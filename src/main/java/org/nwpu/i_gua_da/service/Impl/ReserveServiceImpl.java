package org.nwpu.i_gua_da.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Reserve;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.mapper.ReserveMapper;
import org.nwpu.i_gua_da.mapper.ScheduleMapper;
import org.nwpu.i_gua_da.service.AdminService;
import org.nwpu.i_gua_da.service.ReserveService;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ReserveServiceImpl implements ReserveService {

    @Value("${redisKey.lastSeat}")
    private String lastSeatRedisKey;
    @Value("${constants.reserve.status.default}")
    private int reserveDefaultStatus;
    @Value("${constants.reserve.status.isDelete}")
    private int reserveIsDeleteStatus;

    @Autowired
    private ReserveMapper reserveMapper;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private AdminService adminService;
    //因为scheduleService没有对应的api, 暂使用scheduleMapper
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public synchronized boolean bookingReserve(Reserve reserve) {
        Schedule schedule;
        if(reserve == null || reserve.getSchedule() == null || reserve.getUser() == null ||
                reserve.getSchedule().getScheduleId() == null || reserve.getUser().getUserId() == null)
            throw new NullPointerException();
        if(reserve.getSchedule().getScheduleId() < 0 || reserve.getUser().getUserId() < 0)
            throw new IllegalArgumentException();
        if(adminService.searchUser(reserve.getUser().getUserId()) == null)
            throw new RuntimeException("该用户不存在");
        if ((schedule=scheduleService.getScheduleId(reserve.getSchedule().getScheduleId()))==null){
            throw new RuntimeException("该班车不存在");
        }
        if(reserve.getReserveTime() == null)
            reserve.setReserveTime(LocalDateTime.now());
        if(reserve.getStatus() == null)
            reserve.setStatus(reserveDefaultStatus);
        int lastSeat = schedule.getLastSeat();
        //剩余座位不足
        if(lastSeat < 1)
            return false;
        schedule.setLastSeat(lastSeat-1);
        //更新数据库
        scheduleMapper.updateLastSeatByScheduleId(schedule);
        return reserveMapper.insertReserve(reserve) == 1;
    }

    @Override
    public PageInfo<Reserve> getUserReserve(Integer userId, LocalDateTime startTime, LocalDateTime endTime, Integer pageNum, Integer pageSize) {
        if(userId == null || pageNum == null || pageSize == null || startTime == null || endTime == null)
            throw new NullPointerException();
        if(userId < 1 || pageNum < 1 || pageSize < 1 || startTime.isAfter(endTime))
            throw new IllegalArgumentException();
        PageHelper.startPage(pageNum, pageSize);
        List<Reserve> reserves = reserveMapper.listReservesByUserIdBetweenTimes(userId, startTime, endTime);
        PageInfo<Reserve> pageInfo = new PageInfo<>(reserves);
        return pageInfo;
    }
    public PageInfo<Reserve> getAllReserves(Integer userId,Integer pageNum, Integer pageSize,
                                            LocalDateTime startDateTime,LocalDateTime endDateTime){
        if (userId == null || pageNum == null || pageSize == null){
            throw new NullPointerException();
        }
        if(userId < 1 || pageNum < 1 || pageSize < 1){
            throw new IllegalArgumentException();
        }
        System.out.println(startDateTime);
        System.out.println(endDateTime);
        PageHelper.startPage(pageNum,pageSize);
        List<Reserve> reserves = reserveMapper.listReservesByUserIdBetweenTimes(userId,startDateTime, endDateTime);
        PageInfo<Reserve> pageInfo = new PageInfo<>(reserves);
        return pageInfo;
    }

    @Override
    public boolean removeReserve(Integer userId, Integer scheduleId) {
        if(userId == null || scheduleId == null)
            throw new NullPointerException();
        if(userId < 1 || scheduleId < 0)
            throw new IllegalArgumentException();
        //检测用户是否存在
        if(adminService.searchUser(userId) == null)
            throw new RuntimeException("该用户不存在");
        //在lastSeat中添加一个座位
        Integer lastSeat = (Integer) redisTemplate.opsForValue().get(lastSeatRedisKey+":"+scheduleId);
        if(lastSeat == null) {
            Schedule schedule = scheduleService.getScheduleId(scheduleId);
            if(schedule == null)
                throw new RuntimeException("该班次不存在");
            Integer dbLastSeat = schedule.getLastSeat();
            if(dbLastSeat == null)
                throw new RuntimeException("该班次不存在");
            else {
                //根据dbLastSeat更新缓存和数据库中的lastSeat
                redisTemplate.opsForValue().set(lastSeatRedisKey+":"+scheduleId, dbLastSeat+1);
                Schedule newSchedule = new Schedule();
                newSchedule.setScheduleId(scheduleId);
                newSchedule.setLastSeat(dbLastSeat+1);
                scheduleMapper.updateLastSeatByScheduleId(newSchedule);
            }
        } else {
            //lastSeat更新缓存和数据库中的lastSeat
            redisTemplate.opsForValue().set(lastSeatRedisKey+":"+scheduleId, lastSeat+1);
            Schedule schedule = new Schedule();
            schedule.setScheduleId(scheduleId);
            schedule.setLastSeat(lastSeat+1);
            scheduleMapper.updateLastSeatByScheduleId(schedule);
        }
        int i = reserveMapper.setStatusByUserIdAndScheduleId(userId, scheduleId, reserveIsDeleteStatus);
        return i == 1;
    }

    @Override
    public boolean removeReserveByReserveId(Integer reserveId) {
        if(reserveId == null)
            throw new NullPointerException();
        if(reserveId < 0)
            throw new IllegalArgumentException();
        Reserve reserve = reserveMapper.selectReserveByReserveId(reserveId);
        if (reserve==null){
            throw new RuntimeException("该预约不存在");
        }
        Schedule schedule = reserve.getSchedule();
        schedule.setLastSeat(schedule.getLastSeat()+1);
        scheduleMapper.updateLastSeatByScheduleId(schedule);
        return reserveMapper.setStatusByReserveId(reserveId, reserveIsDeleteStatus) == 1;
    }

    @Override
    public boolean verifyByScheduleIdAndUserId(Integer scheduleId, Integer userId) {
        if(userId == null || scheduleId == null)
            throw new NullPointerException();
        if(userId < 0 || scheduleId < 0)
            throw new IllegalArgumentException();
        //检测用户是否存在
        if(adminService.searchUser(userId) == null)
            throw new RuntimeException("该用户不存在");
        return reserveMapper.verifyReserveByScheduleIdAndUserId(userId, scheduleId) != null;
    }

    @Override
    public List<Reserve> getUserReserveByUserIdAndScheduleId(Integer userId, Integer scheduleId) {
        if(userId == null || scheduleId == null)
            throw new NullPointerException();
        if(userId < 0 || scheduleId < 0)
            throw new IllegalArgumentException();
        System.out.println("scheduleId==>"+scheduleId);
        return reserveMapper.selectReserveByUserIdAndScheduleId(userId,scheduleId);
    }

    @Override
    public void removeReserveByScheduleId(int scheduledId) {
        if (scheduledId<0){
            throw new IllegalArgumentException();
        }
        reserveMapper.removeByScheduleId(scheduledId);
    }
}

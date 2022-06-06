package org.nwpu.i_gua_da.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Reserve;
import org.nwpu.i_gua_da.entity.Schedule;
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
    public boolean bookingReserve(Reserve reserve) {
        if(reserve == null || reserve.getSchedule() == null || reserve.getUser() == null ||
                reserve.getSchedule().getScheduleId() == null || reserve.getUser().getUserId() == null)
            throw new NullPointerException();
        if(reserve.getSchedule().getScheduleId() < 0 || reserve.getUser().getUserId() < 0)
            throw new IllegalArgumentException();
        if(adminService.searchUser(reserve.getUser().getUserId()) == null)
            throw new RuntimeException("该用户不存在");
        if(reserve.getReserveTime() == null)
            reserve.setReserveTime(LocalDateTime.now());
        if(reserve.getStatus() == null)
            reserve.setStatus(reserveDefaultStatus);
        //从redis获取对应的lastSeat
        Integer lastSeat = (Integer) redisTemplate.opsForValue().get(lastSeatRedisKey+":"+reserve.getSchedule().getScheduleId());
        //lastSeat==null, 在数据库查找
        if(lastSeat == null) {
            lastSeat = scheduleService.getScheduleId(reserve.getSchedule().getScheduleId()).getLastSeat();
            if(lastSeat == null)
                throw new RuntimeException("该班次不存在");
        } else {
            //剩余座位不足
            if(lastSeat < 1)
                return false;
            else
                //更新缓存
                redisTemplate.opsForValue().set(lastSeatRedisKey+":"+reserve.getSchedule().getScheduleId(), lastSeat-1, 30, TimeUnit.MINUTES);
        }
        Schedule schedule = new Schedule();
        schedule.setScheduleId(reserve.getSchedule().getScheduleId());
        schedule.setLastSeat(lastSeat-1);
        //更新数据库
        scheduleMapper.updateLastSeatByScheduleId(schedule);
        int i = reserveMapper.insertReserve(reserve);
        return i == 1;
    }

    @Override
    public List<Reserve> getUserReserve(Integer userId, LocalDateTime startTime, LocalDateTime endTime, Integer pageNum, Integer pageSize) {
        if(userId == null || pageNum == null || pageSize == null || startTime == null || endTime == null)
            throw new NullPointerException();
        if(userId < 0 || pageNum < 0 || pageSize < 1 || startTime.isAfter(endTime))
            throw new IllegalArgumentException();
        PageHelper.startPage(pageNum, pageSize);
        List<Reserve> reserves = reserveMapper.listReservesByUserIdBetweenTimes(userId, startTime, endTime);
        PageInfo<Reserve> pageInfo = new PageInfo<>(reserves);
        return pageInfo.getList();
    }

    @Override
    public boolean removeReserve(Integer userId, Integer scheduleId) {
        if(userId == null || scheduleId == null)
            throw new NullPointerException();
        if(userId < 0 || scheduleId < 0)
            throw new IllegalArgumentException();
        //检测用户是否存在
        if(adminService.searchUser(userId) == null)
            throw new RuntimeException("该用户不存在");
        //在lastSeat中添加一个座位
        Integer lastSeat = (Integer) redisTemplate.opsForValue().get(lastSeatRedisKey+":"+scheduleId);
        if(lastSeat == null) {
            Integer dbLastSeat = scheduleService.getScheduleId(scheduleId).getLastSeat();
            if(dbLastSeat == null)
                throw new RuntimeException("该班次不存在");
            else {
                //根据dbLastSeat更新缓存和数据库中的lastSeat
                redisTemplate.opsForValue().set(lastSeatRedisKey+":"+scheduleId, dbLastSeat+1);
                Schedule schedule = new Schedule();
                schedule.setScheduleId(scheduleId);
                schedule.setLastSeat(dbLastSeat+1);
                scheduleMapper.updateLastSeatByScheduleId(schedule);
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
}

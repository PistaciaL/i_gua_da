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

@Service
public class ReserveServiceImpl implements ReserveService {

    @Value("${redisKey.lastSeat}")
    private String lastSeatRedisKey;
    @Value("${constants.reserve.status.default}")
    private int reserveDefaultStatus;

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
        Integer lastSeat = (Integer) redisTemplate.opsForHash().get(lastSeatRedisKey, reserve.getSchedule().getScheduleId());
        if(lastSeat != null && lastSeat <= 0)
            return false;
        redisTemplate.opsForHash().put(lastSeatRedisKey, reserve.getSchedule().getScheduleId(), lastSeat-1);
        if(lastSeat == null && scheduleService.getScheduleId(reserve.getSchedule().getScheduleId()) == null) {
            redisTemplate.opsForHash().put(lastSeatRedisKey, reserve.getSchedule().getScheduleId(), lastSeat);
            throw new RuntimeException("该班次不存在");
        }
        Schedule schedule = new Schedule();
        schedule.setScheduleId(reserve.getSchedule().getScheduleId());
        schedule.setLastSeat(lastSeat-1);
        scheduleMapper.updateLastSeatByScheduleId(schedule);
        int i = reserveMapper.insertReserve(reserve);
        return i == 1;
    }

    @Override
    public List<Reserve> getUserReserve(Integer userId, Integer pageNum, Integer pageSize) {
        if(userId == null || pageNum == null || pageSize == null)
            throw new NullPointerException();
        if(userId < 0 || pageNum < 0 || pageSize < 1)
            throw new IllegalArgumentException();
        PageHelper.startPage(pageNum, pageSize);
        List<Reserve> reserves = reserveMapper.listReservesByUserId(userId);
        PageInfo<Reserve> pageInfo = new PageInfo<>(reserves);
        return pageInfo.getList();
    }

    @Override
    public boolean removeReserve(Integer userId, Integer scheduleId) {
        if(userId == null || scheduleId == null)
            throw new NullPointerException();
        if(userId < 0 || scheduleId < 0)
            throw new IllegalArgumentException();
        int i = reserveMapper.deleteReserveByUserIdAndScheduleId(userId, scheduleId);
        return i == 1;
    }
}

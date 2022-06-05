package org.nwpu.i_gua_da.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.Station;
import org.nwpu.i_gua_da.mapper.ScheduleMapper;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Value("${constants.schedule.status.default}")
    private int statusDefault;
    @Value("${redisKey.station}")
    private String stationRedisKey;
    @Value("${redisKey.lastSeat}")
    private String lastSeatRedisKey;

    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Schedule> getSchedule(Integer pageNum, Integer pageSize) {
        if(pageNum == null || pageSize == null)
            throw new NullPointerException();
        if(pageNum < 0 || pageSize <= 0)
            throw new IllegalArgumentException();
        PageHelper.startPage(pageNum, pageSize);
        List<Schedule> schedules = scheduleMapper.listAllSchedules();
        PageInfo<Schedule> pageInfo = new PageInfo<>(schedules);
        return pageInfo.getList();
    }

    @Override
    public List<Schedule> findSchedule(LocalDateTime startTime, LocalDateTime endTime, Integer pageNum, Integer pageSize) {
        if(startTime == null || endTime == null)
            throw new NullPointerException();
        if(endTime.isAfter(LocalDateTime.now()) || startTime.isAfter(endTime))
            throw new IllegalArgumentException();
        PageHelper.startPage(pageNum, pageSize);
        List<Schedule> schedules = scheduleMapper.listScheduleBetweenTimes(startTime, endTime);
        PageInfo<Schedule> pageInfo = new PageInfo<>(schedules);
        return pageInfo.getList();
    }

    @Override
    public Schedule getScheduleId(Integer scheduledId) {
        if(scheduledId == null)
            throw new NullPointerException();
        if(scheduledId < 0)
            throw new IllegalArgumentException();
        return scheduleMapper.getSchedulesByScheduleId(scheduledId);
    }

    /**
     * 添加一个班次
     * @param schedule
     * @return 是否添加成功
     */
    public boolean addSchedule(Schedule schedule) {
        if(schedule == null || schedule.getStartStation() == null || schedule.getEndStation() == null || schedule.getTotalSeat() == null)
            throw new NullPointerException();
        if(schedule.getTotalSeat() <= 0 || (schedule.getLastSeat() != null && schedule.getLastSeat() < 0))
            throw new IllegalArgumentException();
        if(redisTemplate.opsForHash().get(stationRedisKey, schedule.getStartStation().getStationId()) == null ||
                redisTemplate.opsForHash().get(stationRedisKey, schedule.getEndStation().getStationId()) == null) {
            //在数据库里查找station是否存在, (不存在为true)
            Station start = null;
            Station end = null;
            if(true)
                throw new RuntimeException("班次里的车站不存在");
            else {
                boolean isEmpty = false;
                if(redisTemplate.opsForHash().keys(stationRedisKey) == null)
                    isEmpty = true;
                redisTemplate.opsForHash().put(stationRedisKey, start.getStationId(), start);
                redisTemplate.opsForHash().put(stationRedisKey, end.getStationId(), end);
                if(isEmpty)
                    redisTemplate.expire(stationRedisKey, 1, TimeUnit.DAYS);
            }
        }
        if(schedule.getDepartureTime() == null)
            schedule.setDepartureTime(LocalDateTime.now());
        if(schedule.getLastSeat() == null)
            schedule.setLastSeat(schedule.getTotalSeat());
        if(schedule.getStatus() == null)
            schedule.setStatus(statusDefault);
        Integer id = scheduleMapper.addSchedule(schedule);
        if(id != null) {
            if(schedule.getLastSeat() > 0)
                redisTemplate.opsForHash().put(lastSeatRedisKey, id, schedule.getLastSeat());
            return true;
        }
        return false;
    }
}

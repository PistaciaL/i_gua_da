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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Value("${constants.schedule.status.default}")
    private int statusDefault;
    @Value("${constants.schedule.status.isDelete}")
    private int statusIsDelete;
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
        if(startTime.isAfter(endTime))
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

    @Override
    @Transactional
    public boolean addSchcedule(Schedule schedule) {
        if(schedule == null || schedule.getStartStation() == null || schedule.getEndStation() == null || schedule.getTotalSeat() == null)
            throw new NullPointerException();
        if(schedule.getTotalSeat() <= 0 || (schedule.getLastSeat() != null && schedule.getLastSeat() < 0))
            throw new IllegalArgumentException();
        //如果在数据库里找不到对应车站的信息
        Station startStation = null;
        Station endStation = null;
        if((startStation = (Station) redisTemplate.opsForValue().get(stationRedisKey+":"+schedule.getStartStation().getStationId())) == null ||
                (endStation = (Station) redisTemplate.opsForValue().get(stationRedisKey+":"+schedule.getEndStation().getStationId())) == null) {
            //在数据库里查找station是否存在, (不存在为true)
            if(startStation == null) {
                startStation = null;
                if(startStation != null) {
                    redisTemplate.opsForValue().set(stationRedisKey+":"+schedule.getStartStation().getStationId(), startStation, 1, TimeUnit.DAYS);
                }
            }
            if(endStation == null) {
                endStation = null;
                if(endStation != null) {
                    redisTemplate.opsForValue().set(stationRedisKey + ":" + schedule.getEndStation().getStationId(), endStation, 1, TimeUnit.DAYS);
                }
            }
            if(startStation == null || endStation == null)
                throw new RuntimeException("班次里的车站不存在");
        }
        if(schedule.getDepartureTime() == null)
            schedule.setDepartureTime(LocalDateTime.now());
        if(schedule.getLastSeat() == null)
            schedule.setLastSeat(schedule.getTotalSeat());
        if(schedule.getStatus() == null)
            schedule.setStatus(statusDefault);
        //获取自动生成的key(schedule_id)
        Integer id = scheduleMapper.addSchedule(schedule);
        if(id != null) {
            if(schedule.getLastSeat() > 0)
                redisTemplate.opsForValue().set(lastSeatRedisKey+":"+id, schedule.getLastSeat(), 30, TimeUnit.MINUTES);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean removeSchcedule(Integer scheduledId) {
        if(scheduledId == null)
            throw new NullPointerException();
        if(scheduledId < 0)
            throw new IllegalArgumentException();
        redisTemplate.delete(lastSeatRedisKey+":"+scheduledId);
        int i = scheduleMapper.setStatusByScheduleId(scheduledId, statusIsDelete);
        return i == 1;
    }
}
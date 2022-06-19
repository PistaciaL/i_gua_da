package org.nwpu.i_gua_da.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.Station;
import org.nwpu.i_gua_da.mapper.ScheduleMapper;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.nwpu.i_gua_da.service.StationService;
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
    @Value("${constants.schedule.status.notDelete}")
    private int statusNotDelete;
    @Value("${redisKey.station}")
    private String stationRedisKey;
    @Value("${redisKey.lastSeat}")
    private String lastSeatRedisKey;
    @Value("${constants.schedule.totalSeat.default}")
    private int totalSeatDefault;

    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StationService stationService;

    @Override
    public PageInfo<Schedule> getSchedule(Integer pageNum, Integer pageSize) {
    	if(pageNum == null || pageSize == null){
            throw new NullPointerException();
        }
        if (pageNum<1||pageSize<1){
            throw new IllegalArgumentException();
        }
        PageHelper.startPage(pageNum-1, pageSize);
        List<Schedule> schedules = scheduleMapper.listAllSchedules();
        PageInfo<Schedule> pageInfo = new PageInfo<>(schedules);
        return pageInfo;
    }

    @Override
    public PageInfo<Schedule> findSchedule(LocalDateTime startTime, LocalDateTime endTime, Integer pageNum, Integer pageSize) {
    	if(startTime == null || endTime == null)
            throw new NullPointerException();
        if(startTime.isAfter(endTime))
            throw new IllegalArgumentException();
        if (pageNum<1||pageSize<1){
            throw new IllegalArgumentException();
        }
        PageHelper.startPage(pageNum-1, pageSize);
        List<Schedule> schedules = scheduleMapper.listScheduleBetweenTimes(startTime, endTime, statusNotDelete);
        PageInfo<Schedule> pageInfo = new PageInfo<>(schedules);
        return pageInfo;
    }

    @Override
	public PageInfo<Schedule> findScheduleByStationAndTime(LocalDateTime startTime, LocalDateTime endTime,
			String startStationName, String endStationName, Integer pageNum, Integer pageSize) {
		if(startTime == null || endTime == null || startStationName == null || endStationName == null)
			throw new NullPointerException();
		if(startTime.isAfter(endTime))
			throw new IllegalArgumentException();
        if (pageNum<1||pageSize<1){
            throw new IllegalArgumentException();
        }
		PageHelper.startPage(pageNum-1, pageSize);
        List<Schedule> schedules = scheduleMapper.listScheduleByStationAndTime(startTime, endTime, 
        		statusNotDelete, startStationName, endStationName);
        PageInfo<Schedule> pageInfo = new PageInfo<>(schedules);
        return pageInfo;
	}
    
    @Override
    public Schedule getScheduleId(Integer scheduledId) {
    	if(scheduledId == null)
            throw new NullPointerException();
        if(scheduledId < 1)
            throw new IllegalArgumentException();
        return scheduleMapper.getSchedulesByScheduleId(scheduledId);
    }

    @Override
    @Transactional
    public boolean addSchcedule(Schedule schedule) {
    	if(schedule == null || schedule.getStartStation() == null || schedule.getEndStation() == null)
            throw new NullPointerException();
        if(schedule.getTotalSeat() == null)
            schedule.setTotalSeat(totalSeatDefault);
        if(schedule.getTotalSeat() <= 0 || (schedule.getLastSeat() != null && schedule.getLastSeat() < 0))
            throw new IllegalArgumentException();
        Station startStation = null;
        Station endStation = null;
        //如果在redis里找不到对应车站的信息
        if((startStation = (Station) redisTemplate.opsForValue().get(stationRedisKey+":"+schedule.getStartStation().getStationId())) == null ||
                (endStation = (Station) redisTemplate.opsForValue().get(stationRedisKey+":"+schedule.getEndStation().getStationId())) == null) {
            //在数据库里查找station是否存在, (不存在为true)
            if(startStation == null) {
                startStation = stationService.selectStationByStationId(schedule.getStartStation().getStationId());
                if(startStation != null) {
                    redisTemplate.opsForValue().set(stationRedisKey+":"+schedule.getStartStation().getStationId(), startStation, 10, TimeUnit.MINUTES);
                }
            }
            if(endStation == null) {
                endStation = stationService.selectStationByStationId(schedule.getEndStation().getStationId());
                if(endStation != null) {
                    redisTemplate.opsForValue().set(stationRedisKey + ":" + schedule.getEndStation().getStationId(), endStation, 10, TimeUnit.MINUTES);
                }
            }
            if(startStation == null || endStation == null)
                throw new RuntimeException("班次里的车站不存在");
        }
        if(schedule.getDepartureDateTime() == null)
            schedule.setDepartureDateTime(LocalDateTime.now());
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

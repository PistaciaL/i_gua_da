package org.nwpu.i_gua_da.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.mapper.ScheduleMapper;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

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
    public List<Schedule> findSchedule(LocalDateTime startTime, LocalDateTime endTime) {
        if(startTime == null || endTime == null)
            throw new NullPointerException();
        if(endTime.isAfter(LocalDateTime.now()) || startTime.isAfter(endTime))
            throw new IllegalArgumentException();
        //暂时没有分页
        List<Schedule> schedules = scheduleMapper.listScheduleBetweenTimes(startTime, endTime);
        return schedules;
    }

    @Override
    public boolean bookingSchedule(Integer ScheduleId) {
        return false;
    }

    @Override
    public List<Schedule> getUserSchedule(String username) {
        return null;
    }

    @Override
    public boolean removeSchedule(Integer scheduleId) {
        return false;
    }
}

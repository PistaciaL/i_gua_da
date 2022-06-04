package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.Reserve;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.mapper.ReserveMapper;
import org.nwpu.i_gua_da.mapper.ScheduleMapper;
import org.nwpu.i_gua_da.service.AdminService;
import org.nwpu.i_gua_da.service.ReserveService;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReserveServiceImpl implements ReserveService {

    @Autowired
    private ReserveMapper reserveMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private AdminService adminService;

    @Override
    @Transactional
    public boolean bookingSchedule(Reserve reserve) {
        if(reserve == null || reserve.getSchedule() == null || reserve.getUser() == null ||
                reserve.getSchedule().getScheduleId() == null || reserve.getUser().getUserId() == null)
            throw new NullPointerException();
        if(reserve.getSchedule().getScheduleId() < 0 || reserve.getUser().getUserId() < 0)
            throw new IllegalArgumentException();
        if(adminService.searchUser(reserve.getUser().getUserId()) == null)
            throw new RuntimeException("该用户不存在");
        //判断是否存在对应班次
        if(false)
            throw new RuntimeException("该班次不存在");
        int i = reserveMapper.insertReserve(reserve);
        return i == 1;
    }

    @Override
    @Transactional
    public List<Schedule> getUserSchedule(User user) {
//        if(user == null || user.getUserId() == null)
//            throw new NullPointerException();
//        if(user.getUserId() < 0)
//            throw new IllegalArgumentException();
//        if(adminService.searchUser(user.getUserId()) == null)
//            throw new RuntimeException("该用户不存在");
//        return scheduleMapper.getSchedulesByUserId(user.getUserId());
        return null;
    }

    @Override
    public boolean removeReserve(User user, Integer scheduleId) {
        return false;
    }
}

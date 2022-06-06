package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.api.CommonResult;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 获取当日校车班次信息
 */
@RestController
public class GetScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 首页展示的当日班车
     * @return 当日班车前6个列表
     */
    @RequestMapping("/getTodayTimes")
    public CommonResult GetSchedule(){
        List<Schedule> schedules;
        schedules = scheduleService.getSchedule(1,6);
        if (schedules == null){
            return CommonResult.failed();
        }
        return CommonResult.success(schedules);
    }
}

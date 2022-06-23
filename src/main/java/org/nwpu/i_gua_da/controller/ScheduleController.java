package org.nwpu.i_gua_da.controller;

import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.nwpu.i_gua_da.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.*;
import org.nwpu.i_gua_da.fastjson.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 班车事务控制类
 */
@RestController
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private StationService stationService;

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    DateTimeFormatter dfOut = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.CHINA);

    /**
     * 获取所有当日未被取消的班车时刻表
     * @param page 要获取的班次表的页码
     * @param pageSize 每页的数据条数
     * @return
     */
    @RequestMapping("/getTodaySchedule")
    public String UserSearchTime(@RequestParam("page") int page,
                                 @RequestParam("pageSize") int pageSize) {
        PageInfo<Schedule> schedules = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        LocalDate nowDate = LocalDateTime.now().toLocalDate();
        startTime = nowDate.atTime(00, 00);
        endTime = nowDate.atTime(23, 59);
        schedules = scheduleService.findSchedule(startTime, endTime, page, pageSize);
        GetTodaySchedule getTodaySchedule = new GetTodaySchedule();
        getTodaySchedule.setStatus(200);
        getTodaySchedule.setPage(schedules.getPageNum());
        getTodaySchedule.setTotalPageNumb(schedules.getPages());
        List<ScheduleData> listScheduleData = new ArrayList<ScheduleData>();
        ScheduleData scheduleData = new ScheduleData();
        for(Schedule schedule : schedules.getList()) {
            scheduleData = new ScheduleData();
            scheduleData.setScheduleId(schedule.getScheduleId());
            scheduleData.setStartCampus(schedule.getStartStation().getCampus());
            scheduleData.setStartStation(schedule.getStartStation().getStationName());
            scheduleData.setEndCampus(schedule.getEndStation().getCampus());
            scheduleData.setEndStation(schedule.getEndStation().getStationName());
            scheduleData.setDepartureDatetime(schedule.getDepartureDateTime().format(dfOut));
            listScheduleData.add(scheduleData);
        }
        getTodaySchedule.setData(listScheduleData);
        return JSON.toJSONString(getTodaySchedule);
    }

    /**
     * 查询班次接口
     * @param startCampus 班次起始校区
     * @param endCampus 班次终点校区
     * @param date 班次出发时刻，精确到天
     * @param page 要查询的班次页码
     * @param pageSize 每页的数据条数
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/searchSchedule")
    public String UserSearchTimeByCampus(@RequestParam("startCampus") String startCampus,
                                         @RequestParam("endCampus") String endCampus, @RequestParam(value = "departureDate", defaultValue = "2022/01/01 00") String date,
                                         @RequestParam("page") int page,
                                         @RequestParam("pageSize") int pageSize,
                                         @RequestParam("code")String code) {
        PageInfo<Schedule> schedules = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        LocalDate departureDate = LocalDate.parse(date, fmt);
        startTime = departureDate.atTime(00, 00);
        endTime = departureDate.atTime(23, 59);
        schedules = scheduleService.findScheduleByStationAndTime(startTime, endTime,
                startCampus, endCampus, page, pageSize);
        SearchSchedule searchSchedule = new SearchSchedule();
        searchSchedule.setStatus(200);
        searchSchedule.setPage(schedules.getPageNum());
        searchSchedule.setTotalPageNumb(schedules.getPages());
        List<ScheduleSeatData> listScheduleData = new ArrayList<ScheduleSeatData>();
        ScheduleSeatData scheduleData = new ScheduleSeatData();
        for(Schedule schedule : schedules.getList()) {
            scheduleData = new ScheduleSeatData();
            scheduleData.setScheduleId(schedule.getScheduleId());
            scheduleData.setStartStation(schedule.getStartStation().getStationName());
            scheduleData.setEndStation(schedule.getEndStation().getStationName());
            scheduleData.setDepartureDatetime(schedule.getDepartureDateTime().format(dfOut));
            scheduleData.setLastSeat(schedule.getLastSeat());
            listScheduleData.add(scheduleData);
        }
        searchSchedule.setData(listScheduleData);
        return JSON.toJSONString(searchSchedule);
    }

    /**
     * 管理员搜素班车列表接口
     * @param startDate 要搜素的时间区间的左边界
     * @param endDate  要搜素的时间区间的右边界
     * @param page 搜素班次的页码
     * @param pageSize 每页的数据条数
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/manager/searchSchedules")
    public String AdminSearchTimeByCampus(@RequestParam(value = "startDate", defaultValue = "1970/01/01") String startDate,
                                          @RequestParam(value = "endDate", defaultValue = "2200/01/01") String endDate,
                                          @RequestParam("page") int page,
                                          @RequestParam("pageSize") int pageSize,
                                          @RequestParam("code")String code) {
        PageInfo<Schedule> schedules = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        LocalDate startDepartureDate = LocalDate.parse(startDate, fmt);
        LocalDate endDepartureDate = LocalDate.parse(endDate, fmt);
        startTime = startDepartureDate.atTime(00, 00);
        endTime = endDepartureDate.atTime(23, 59);
        schedules = scheduleService.findSchedule(startTime,endTime,page,pageSize);
        SearchSchedules searchSchedule = new SearchSchedules();
        searchSchedule.setStatus(200);
        searchSchedule.setPage(schedules.getPageNum());
        searchSchedule.setTotalPageNumb(schedules.getPages());
        List<AdminScheduleData> listScheduleData = new ArrayList<AdminScheduleData>();
        AdminScheduleData scheduleData = new AdminScheduleData();
        for(Schedule schedule : schedules.getList()) {
            scheduleData = new AdminScheduleData();
            scheduleData.setScheduleId(schedule.getScheduleId());
            scheduleData.setStartCampus(schedule.getStartStation().getCampus());
            scheduleData.setStartStation(schedule.getStartStation().getStationName());
            scheduleData.setEndCampus(schedule.getEndStation().getCampus());
            scheduleData.setEndStation(schedule.getEndStation().getStationName());
            scheduleData.setDepartureDatetime(schedule.getDepartureDateTime().format(dfOut));
            scheduleData.setLastSeat(schedule.getLastSeat());
            listScheduleData.add(scheduleData);
        }
        searchSchedule.setData(listScheduleData);
        return JSON.toJSONString(searchSchedule);
    }

}

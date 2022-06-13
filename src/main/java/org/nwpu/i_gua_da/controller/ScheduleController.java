package org.nwpu.i_gua_da.controller;

import com.github.pagehelper.Page;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.AdminService;
import org.nwpu.i_gua_da.service.ReserveService;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.nwpu.i_gua_da.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.StringJoiner;

/**
 * 班车事务
 */
@RestController
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ReserveService reserveService;

    DateTimeFormatter dfIn = DateTimeFormatter.ofPattern("yyyy/M/d HH", Locale.CHINA);
    DateTimeFormatter dfOut = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.CHINA);

    /**
     * 用户查看所有校车班车
     * @param page 表示要返回的页码
     * @param number 表示每页的数据条数
     * @param StartTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param EndTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param session 用户session
     * @return status = 1表示查询成功，status = 0表示起始时间大于终止时间或时间格式有误导致查询错误，status=2表示用户session失效
     */
    @RequestMapping("/user/searchTimes")
    public String UserSearchTime(@RequestParam("page") int page, @RequestParam("number") int number,
                                 @RequestParam(value = "startTime", defaultValue = "2022/01/01 00") String StartTime,
                                 @RequestParam(value = "endTime", required = false) String EndTime, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return "{\"status\":2}";
        }else {
            User user = adminService.searchUser(userId);
            Page<Schedule> schedules = null;
            try {
                LocalDateTime startTime = null;
                LocalDateTime endTime = null;
                if(EndTime == null || "".equals(EndTime))
                    endTime = LocalDateTime.now();
                else {
                    try {
                        endTime = LocalDateTime.parse(EndTime,dfIn);
                    } catch (DateTimeParseException e) {
                        EndTime += " 00";
                        endTime = LocalDateTime.parse(EndTime,dfIn);
                    }
                }
                try {
                    startTime = LocalDateTime.parse(StartTime,dfIn);
                } catch (DateTimeParseException e) {
                    StartTime += " 00";
                    startTime = LocalDateTime.parse(StartTime, dfIn);
                }
                schedules = (Page) scheduleService.findSchedule(startTime, endTime, page, number);
            } catch (NullPointerException e) {
                return "{\"status\":0}";
            } catch (IllegalArgumentException e) {
                return "{\"status\":0}";
            }
            StringJoiner mainSj = new StringJoiner(",", "{", "}");
            mainSj.add("\"status\":1");
            mainSj.add("\"currentPage\":"+schedules.getPageNum());
            mainSj.add("\"pages\":"+schedules.getPages());
            StringJoiner sj = new StringJoiner(",", "\"times\": [", "]");
            for(Schedule schedule : schedules) {
                StringJoiner sj1 = new StringJoiner(",", "{", "}");
                sj1.add("\"id\":"+schedule.getScheduleId());
                sj1.add("\"time\":\""+schedule.getDepartureTime().format(dfOut)+"\"");
                sj1.add("\"start\":\""+schedule.getStartStation().getStationName()+"\"");
                sj1.add("\"end\":\""+schedule.getEndStation().getStationName()+"\"");
                sj1.add("\"number\":"+schedule.getLastSeat());
                boolean b = reserveService.verifyByScheduleIdAndUserId(schedule.getScheduleId(), user.getUserId());
                sj1.add("\"hasOrdered\":"+ (b ? "true" : "false"));
                sj.add(sj1.toString());
            }
            mainSj.add(sj.toString());
            return mainSj.toString();
        }
    }
}

package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.api.CommonResult;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.nwpu.i_gua_da.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 班车事务
 */
@RestController
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/mm/dd");

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
    public CommonResult UserSearchTime(int page, int number, String StartTime, String EndTime, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("2");
        }else {
            User user = userService.getUser((String) LoginUser);
            LocalDateTime startTime = LocalDateTime.parse(StartTime,df);
            LocalDateTime endTime = LocalDateTime.parse(EndTime,df);

            if (StartTime != "" && EndTime != ""){
                if (startTime.compareTo(endTime) < 0){
                    return CommonResult.failed("0");
                }
                List<Schedule> schedules = scheduleService.getSchedule(page,number);

                return CommonResult.success(schedules, "1");
            }else {
                List<Schedule> schedules = scheduleService.findSchedule(startTime,endTime,page,number);

                return CommonResult.success(schedules, "1");
            }
        }
    }
}

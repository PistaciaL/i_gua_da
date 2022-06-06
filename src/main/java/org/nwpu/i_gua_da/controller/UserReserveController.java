package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.api.CommonResult;
import org.nwpu.i_gua_da.entity.Reserve;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.ReserveService;
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
 * 用户预约
 */
@RestController
public class UserReserveController {

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/mm/dd");
    /**
     * 用户查看自己的预约
     * @param page 页码数
     * @param number 每页的条数
     * @param StartTime 开始时间
     * @param EndTime 结束时间
     * @param session 用户session
     * @return status = 1表示查询成功，status = 0表示起始时间大于终止时间或时间格式有误导致查询错误，status=2表示用户session失效
     */
    @RequestMapping("/user/searchOrders")
    public CommonResult SearchOrder(int page, int number, String StartTime, String EndTime, HttpSession session){
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
            }
            List<Reserve> reserves = reserveService.getUserReserve(user.getUserId(), startTime, endTime, page, number);
            List<Schedule> schedules = null;
            for (int i = 0; i < reserves.size(); i++) {
                schedules.add(reserves.get(i).getSchedule());
            }
            return CommonResult.success(schedules,"1");
        }
    }

    /**
     * 用户取消预约
     * @param id 要删除(取消预约)的预约id
     * @param page 删除后返回的页面的页码，因为删除后会导致当前页面数据数目变化，所以需要重新搜索返回页面
     * 这里要特别注意当删除的元素在最后一页且最后一页只有一个元素的情况
     * @param number 每页的数据数目
     * @param StartTime 格式为”yyyy/mm/dd“的字符串，表示时间，当前搜索限制条件
     * @param EndTime 格式为”yyyy/mm/dd“的字符串，表示时间，当前搜索限制条件
     * @param session 用户session
     * @return 状态码status=1表示删除成功，status=0表示删除失败，status=2表示用户session失效
     */
    @RequestMapping("/user/cancel")
    public CommonResult CancelOrder(int id, int page, int number, String StartTime, String EndTime, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("2");
        }else {
            User user = userService.getUser((String) LoginUser);
            LocalDateTime startTime = LocalDateTime.parse(StartTime,df);
            LocalDateTime endTime = LocalDateTime.parse(EndTime,df);

            if (reserveService.removeReserve(user.getUserId(),id)){
                List<Reserve> reserves = reserveService.getUserReserve(user.getUserId(),startTime,endTime,page,number);

                List<Schedule> schedules = null;
                for (int i = 0; i < reserves.size(); i++) {
                    schedules.add(reserves.get(i).getSchedule());
                }

                return CommonResult.success(schedules,"1");
            }else {
                return CommonResult.failed("0");
            }
        }
    }

    /**
     * 用户预约校车
     * @param id 用户预约车的id
     * @param session 用户session
     * @return 状态码，status=1代表预约成功，status=0代表无剩余座位，status=2代表不能重复预约，status=3代表用户session已失效
     */
    @RequestMapping("/user/order")
    public CommonResult Order(int id, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("3");
        }else {
            User user = userService.getUser((String) LoginUser);
            Schedule schedule = scheduleService.getScheduleId(id);
            LocalDateTime time = LocalDateTime.now();

            Reserve reserve = new Reserve();

            reserve.setUser(user);
            reserve.setSchedule(schedule);
            reserve.setReserveTime(time);

            if (reserveService.bookingReserve(reserve)){
                return CommonResult.success("1");
            }else {
                if (schedule.getLastSeat() == 0){
                    return CommonResult.failed("0");
                }
                return CommonResult.failed("2");
            }
        }
    }
}

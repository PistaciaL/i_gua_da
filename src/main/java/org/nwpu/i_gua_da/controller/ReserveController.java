package org.nwpu.i_gua_da.controller;

import com.github.pagehelper.Page;
import org.nwpu.i_gua_da.entity.Reserve;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.AdminService;
import org.nwpu.i_gua_da.service.ReserveService;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.nwpu.i_gua_da.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 用户预约
 */
@RestController
public class ReserveController {

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    @Autowired
    private ScheduleService scheduleService;

    DateTimeFormatter dfIn = DateTimeFormatter.ofPattern("yyyy/M/d HH", Locale.CHINA);
    DateTimeFormatter dfOut = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.CHINA);
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
    public String SearchOrder(@RequestParam("page") int page, @RequestParam("number") int number,
                              @RequestParam(value = "startTime", defaultValue = "2022/01/01 00") String StartTime,
                              @RequestParam(value = "endTime", required = false) String EndTime, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return "{\"status\":2}";
        }else {
            User user = adminService.searchUser(userId);
            Page<Reserve> reserves = null;
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
                reserves = (Page) reserveService.getUserReserve(user.getUserId(), startTime, endTime, page, number);
            } catch (NullPointerException e) {
                return "{\"status\":0}";
            } catch (IllegalArgumentException e) {
                return "{\"status\":0}";
            }
            StringJoiner mainSj = new StringJoiner(",", "{", "}");
            mainSj.add("\"status\":1");
            mainSj.add("\"currentPage\":"+reserves.getPageNum());
            mainSj.add("\"pages\":"+reserves.getPages());
            StringJoiner sj = new StringJoiner(",", "\"orders\": [", "]");
            for(Reserve reserve : reserves) {
                StringJoiner sj1 = new StringJoiner(",", "{", "}");
                sj1.add("\"reserveId\":"+reserve.getReserveId());
                sj1.add("\"id\":"+reserve.getSchedule().getScheduleId());
                sj1.add("\"time\":\""+reserve.getReserveTime().format(dfOut)+"\"");
                sj1.add("\"start\":\""+reserve.getSchedule().getStartStation().getStationName()+"\"");
                sj1.add("\"end\":\""+reserve.getSchedule().getEndStation().getStationName()+"\"");
                String statusStr = "";
                if(reserve.getStatus() == 1)
                    statusStr = "正常";
                else
                    statusStr = "班次被取消";
                sj1.add("\"status\":\""+statusStr+"\"");
                sj.add(sj1.toString());
            }
            mainSj.add(sj.toString());
            return mainSj.toString();
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
    public String CancelOrder(@RequestParam("id") int id, @RequestParam("page") int page, @RequestParam("number") int number,
                                    @RequestParam(value = "startTime", defaultValue = "2022/01/01 00") String StartTime, @RequestParam("endTime") String EndTime, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return "{\"status\":2}";
        }else {
            User user = adminService.searchUser(userId);
            if(user == null)
                return "{\"status\":0}";
            if (reserveService.removeReserveByReserveId(id)){
                return this.SearchOrder(page, number, StartTime, EndTime, session);
            } else
                return "{\"status\":0}";
        }
    }

    /**
     * 用户预约校车
     * @param scheduleId 用户预约车的id
     * @param session 用户session
     * @return 状态码，status=1代表预约成功，status=0代表无剩余座位，status=2代表不能重复预约，status=3代表用户session已失效
     */
    @RequestMapping("/user/order")
    public String Order(@RequestParam("id") int scheduleId, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return "{\"status\":3}";
        }else {
            User user = adminService.searchUser(userId);
            Reserve reserve = new Reserve();
            reserve.setUser(user);
            Schedule schedule = new Schedule();
            schedule.setScheduleId(scheduleId);
            reserve.setSchedule(schedule);
            reserve.setReserveTime(LocalDateTime.now());
            if (reserveService.bookingReserve(reserve)){
                return "{\"status\":1}";
            }else {
                //此处可以使用redis获取剩余座位
                Schedule newSchedule = scheduleService.getScheduleId(scheduleId);
                if (newSchedule.getLastSeat() < 1){
                    return "{\"status\":0}";
                }
                return "{\"status\":2}";
            }
        }
    }
}

package org.nwpu.i_gua_da.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Reserve;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.fastjson.HistoryReserves;
import org.nwpu.i_gua_da.fastjson.ReserveData;
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
import java.util.*;

/**
 * 用户预约控制类
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

    DateTimeFormatter dfOut = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.CHINA);

    /**
     * 获取历史预约接口,即获取用户在当前时间之前发车的预约
     * @param page 要获取的预约的页码
     * @param pageSize 每页的数据条数
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/user/historyReserve")
    public String getHistoryReserves(@RequestParam("page") int page,
                                     @RequestParam("pageSize") int pageSize,
                                     @RequestParam("code") String code){
        //参数校验
        if (page<1 || pageSize<1){
            return "{\"status\":420}";
        }
        User user = userService.getUserByCode(code);
        //根据时间段查找历史记录信息
        PageInfo<Reserve> reserves = reserveService.getAllReserves(user.getUserId(), page, pageSize,
                LocalDateTime.parse("2000/12/01 12:00",dfOut),LocalDateTime.now());
        //返回参数封装
        HistoryReserves historyReserves = new HistoryReserves();
        historyReserves.setStatus(200);
        historyReserves.setPage(reserves.getPageNum());
        historyReserves.setTotalPageNumb(reserves.getPages());
        List<ReserveData> data = new ArrayList<>();
        for (Reserve reserve : reserves.getList()) {
            data.add(new ReserveData(reserve.getReserveId(),
                    reserve.getStatus(),
                    reserve.getSchedule().getStartStation().getCampus(),
                    reserve.getSchedule().getStartStation().getStationName(),
                    reserve.getSchedule().getEndStation().getCampus(),
                    reserve.getSchedule().getEndStation().getStationName(),
                    reserve.getSchedule().getDepartureDateTime().format(dfOut),
                    reserve.getSchedule().getStartStation().getLongitude(),
                    reserve.getSchedule().getEndStation().getLatitude()));
        }
        historyReserves.setData(data);
        return JSON.toJSONString(historyReserves);
    }

    /**
     * 用户取消预约接口
     * @param reserveId 预约id
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/user/cancelReserve")
    public String cancelReserve(@RequestParam("reserveId") String reserveId,
                                @RequestParam("code")String code){
        int id = Integer.valueOf(reserveId);
        if (reserveService.removeReserveByReserveId(id)){
            return "{\"status\":200}";
        }
        return "{\"status\":0}";
    }

    /**
     * 用户预约接口
     * @param scheduleId 要预约的班次id
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/user/reserve")
    public String reserve(@RequestParam("scheduleId") int scheduleId,
                          @RequestParam("code") String code){
        User user = userService.getUserByCode(code);
        //用户信誉值过低无法预约校车
        if (user.getCredit()<8){
            throw new RuntimeException("您的信誉值过低");
        }
        //查看用户预约该班次的记录，如果用户已经预约了就无法再次预约
        List<Reserve> selectedReserve = reserveService.getUserReserveByUserIdAndScheduleId(user.getUserId(),scheduleId);
        if (selectedReserve.size()!=0){
            for (Reserve reserve : selectedReserve) {
                if (reserve.getSchedule().getDepartureDateTime().isAfter(LocalDateTime.now())){
                    System.out.println(selectedReserve.get(0));
                    System.out.println(user.getUserId()+"--"+scheduleId);
                    throw new RuntimeException("不能重复预约");
                }
            }
        }
        //如果班次已发车无法预约
        Schedule selectedSchedule = scheduleService.getScheduleId(scheduleId);
        if (selectedSchedule.getDepartureDateTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("该时间段不能预约");
        }
        Reserve reserve = new Reserve();
        reserve.setUser(user);
        Schedule schedule = new Schedule();
        schedule.setScheduleId(scheduleId);
        reserve.setSchedule(schedule);
        reserve.setReserveTime(LocalDateTime.now());
        if (reserveService.bookingReserve(reserve)){
            return "{\"status\":200}";
        }
        return "{\"status\":420}";
    }

    /**
     * 获取用户当前预约接口
     * @param page 要获取预约的页码
     * @param pageSize 每一页的数据条数
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/user/futureReserve")
    public String getFutureReserves(@RequestParam("page")int page,
                                    @RequestParam("pageSize")int pageSize,
                                    @RequestParam("code")String code){
        if (page<1||pageSize<1){
            return "{\"status\":420";
        }
        User user = userService.getUserByCode(code);
        PageInfo<Reserve> reserves = reserveService.getAllReserves(user.getUserId(), page, pageSize,
                LocalDateTime.now(),LocalDateTime.parse("2050/01/01 12:00",dfOut));
        HistoryReserves historyReserves = new HistoryReserves();
        historyReserves.setStatus(200);
        historyReserves.setPage(reserves.getPageNum());
        historyReserves.setTotalPageNumb(reserves.getPages());
        List<ReserveData> data = new ArrayList<>();
        for (Reserve reserve : reserves.getList()) {
            data.add(new ReserveData(reserve.getReserveId(),reserve.getStatus(),reserve.getSchedule().getStartStation().getCampus(),
                    reserve.getSchedule().getStartStation().getStationName(),reserve.getSchedule().getEndStation().getCampus(),
                    reserve.getSchedule().getEndStation().getStationName(),reserve.getSchedule().getDepartureDateTime().format(dfOut),
                    reserve.getSchedule().getStartStation().getLongitude(),reserve.getSchedule().getStartStation().getLatitude()));
        }
        historyReserves.setData(data);
        return JSON.toJSONString(historyReserves);
    }
}

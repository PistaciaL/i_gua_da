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

    DateTimeFormatter dfOut = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.CHINA);


    @RequestMapping("/user/historyReserve")
    public String getHistoryReserves(@RequestParam("page") int page,
                                     @RequestParam("pageSize") int pageSize,
                                     @RequestParam("code") String code){
        if (page<1 || pageSize<1){
            return "{\"status\":420}";
        }
        User user = userService.getUserByCode(code);
        PageInfo<Reserve> reserves = reserveService.getAllReserves(user.getUserId(), page, pageSize,
                LocalDateTime.parse("2000/12/01 12:00",dfOut),LocalDateTime.now());
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
     * 用户取消预约
     * @return 状态码status=1表示删除成功，status=0表示删除失败，status=2表示用户session失效
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
     * 用户预约校车
     * @param scheduleId 用户预约车的id
     * @return 状态码，status=1代表预约成功，status=0代表无剩余座位，status=2代表不能重复预约，status=3代表用户session已失效
     */
    @RequestMapping("/user/reserve")
    public String reserve(@RequestParam("scheduleId") int scheduleId,
                          @RequestParam("code") String code){
        User user = userService.getUserByCode(code);
        if (user.getCredit()<8){
            throw new RuntimeException("您的信誉值过低");
        }
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

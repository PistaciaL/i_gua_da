package org.nwpu.i_gua_da.controller;

import com.alibaba.fastjson.JSON;
import org.nwpu.i_gua_da.entity.Notice;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.NoticeService;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.nwpu.i_gua_da.service.UserService;
import org.nwpu.i_gua_da.service.UserloginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.StringJoiner;

/**
 * 不需要登录进行的操作
 * 游客操作
 */
@RestController
public class VisitorController {

    @Autowired
    private UserloginService userloginService;
    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private NoticeService noticeService;

    /**
     * 用户登录方法
     * @param userName 用户名
     * @param password 密码
     * @return status=1代表登录成功，status=0代表账户或密码错误，status=2代表该账户被封禁，禁止登录
     * 成功登录的用户的部分信息
     */
    @RequestMapping("/login")
    public String Login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session){
        User user = new User(userName,password);
        if (userloginService.verifyUser(user)){
            User user1 = userService.getUser(userName);
            int i = userService.getUserStatus(user1);
            if (i == 2){
                return "{\"status\":2}";
            }else {
                session.setAttribute("userId",user1.getUserId());
                int permission = userService.getUserpermission(user1.getUserId());
                StringJoiner sj = new StringJoiner(",", "{", "}");
                sj.add("\"status\":1");
                sj.add("\"id\":"+user1.getUserId());
                sj.add("\"userName\":\""+user1.getName()+"\"");
                sj.add("\"userId\":\""+user1.getStudentNumber()+"\"");
                sj.add("\"isManager\":"+user1.getPermission());
                sj.add("\"email\":\""+user1.getEmail()+"\"");
                return sj.toString();
            }
        }else {
            return "{\"status\":0}";
        }
    }

    /**
     * 用户登出，清理sission
     * @param session
     * @param sessionStatus
     * @return status=1表示成功退出登录
     */
    @RequestMapping("/logout")
    public String Logout(HttpSession session, SessionStatus sessionStatus){
        session.invalidate();//让session失效
        sessionStatus.setComplete();//清空session
        return "{\"status\":1}";
    }

    /**
     * 用户注册
     * @param username 用户名
     * @param password 用户密码
     * @param email 用户邮箱
     * @param studentNumber 用户学号
     * @return status=1表示注册成功，status=0表示注册失败(一个邮箱只能注册一次)
     */
    @RequestMapping("/register")
    public String Register(@RequestParam("userName") String username, @RequestParam("password")String password,
                           @RequestParam("email")String email, @RequestParam("userId")String studentNumber, HttpSession session){
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setStudentNumber(studentNumber);
        if (userloginService.addUser(user)){
            StringJoiner sj = new StringJoiner(",", "{", "}");
            sj.add("\"status\":1");
            sj.add("\"id\":"+user.getUserId());
            sj.add("\"userName\":\""+user.getName()+"\"");
            sj.add("\"userId\":\""+user.getStudentNumber()+"\"");
            sj.add("\"isManager\":"+user.getPermission());
            sj.add("\"email\":\""+user.getEmail()+"\"");
            session.setAttribute("userId", user.getUserId());
            return sj.toString();
        }else {
            return "{\"status\":0}";
        }
    }

    /**
     * 首页展示的当日班车
     * @return 当日班车前6个列表
     */
    @RequestMapping("/getTodayTimes")
    public String GetSchedule(){
        List<Schedule> schedules;
        schedules = scheduleService.getSchedule(1,6);
        if (schedules == null){
            return "[]";
        }
        StringJoiner sj = new StringJoiner(",", "[", "]");
        for(Schedule schedule : schedules) {
            StringJoiner sj1 = new StringJoiner(",", "{", "}");
            sj1.add("\"time\":\""+schedule.getDepartureTime().toString()+"\"");
            sj1.add("\"start\":\""+schedule.getStartStation().getStationName()+"\"");
            sj1.add("\"end\":\""+schedule.getEndStation().getStationName()+"\"");
            sj1.add("\"number\":"+schedule.getLastSeat());
            sj.add(sj1.toString());
        }
        return sj.toString();
    }

    /**
     * 获取通知
     * @return 获取时间上最新的且未被删除的五条通知
     */
    @RequestMapping("/getNoitces")
    public String GetNotice(){
        List<Notice> notices;
        notices = noticeService.getNoticeList(1,5);
        if (notices == null){
            return "[]";
        }
        return JSON.toJSONString(notices);
    }
}

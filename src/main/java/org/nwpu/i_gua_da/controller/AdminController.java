package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.api.CommonResult;
import org.nwpu.i_gua_da.entity.Notice;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.Station;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.AdminService;
import org.nwpu.i_gua_da.service.NoticeService;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.nwpu.i_gua_da.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private NoticeService noticeService;

    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/mm/dd");

    /**
     * 用户查看所有校车班车
     * @param page 表示要返回的页码
     * @param number 表示每页的数据条数
     * @param StartTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param EndTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param session 用户session
     * @return status=1表示查询成功，status=0表示输入的日期格式错误或起始日期大于结束日期等，status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/manager/searchTimes")
    public CommonResult AdminSearchTime(int page, int number, String StartTime, String EndTime, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("3");
        }else {
            User user = userService.getUser((String) LoginUser);
            LocalDateTime startTime = LocalDateTime.parse(StartTime,df);
            LocalDateTime endTime = LocalDateTime.parse(EndTime,df);

            int permission = userService.getUserpermission(user.getUserId());

            if (permission == 1){
                return CommonResult.failed("2");
            }else {
                if (StartTime != "" && EndTime != "") {
                    if (startTime.compareTo(endTime) < 0) {
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

    /**
     * 管理员增添班次
     * @param page 表示要返回的页码
     * @param number 表示每页的数据条数
     * @param StartTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param EndTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param time 要添加的班次的出发时间
     * @param start 要添加的班次的始发站
     * @param end 要添加的班次的终点站
     * @param session 用户session
     * @return 状态码，status=1表示添加成功，status=0表示输入数据有误，如起点站和终点站一样，发车时间在现在之前，
     * status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/manager/addTime")
    public CommonResult addTime(int page, int number, String StartTime, String EndTime, String time,
                                String start, String end, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("3");
        }else {
            User user = userService.getUser((String) LoginUser);
            LocalDateTime startTime = LocalDateTime.parse(StartTime,df);
            LocalDateTime endTime = LocalDateTime.parse(EndTime,df);
            LocalDateTime Time = LocalDateTime.parse(time,df);

            int permission = userService.getUserpermission(user.getUserId());

            if (permission == 1){
                return CommonResult.failed("2");
            }else {
                if (start.equals(end)){
                    return CommonResult.failed("0");
                }

                Schedule schedule = new Schedule();
                schedule.setDepartureTime(Time);

                Station startstation = new Station();
                Station endstation = new Station();

                startstation.setStationName(start);
                endstation.setStationName(end);

                schedule.setStartStation(startstation);
                schedule.setEndStation(endstation);

                if (scheduleService.addSchcedule(schedule)){
                    if (StartTime != "" && EndTime != "") {
                        if (startTime.compareTo(endTime) < 0) {
                            return CommonResult.failed("0");
                        }
                        List<Schedule> schedules = scheduleService.getSchedule(page,number);

                        return CommonResult.success(schedules, "1");
                    }else {
                        List<Schedule> schedules = scheduleService.findSchedule(startTime,endTime,page,number);

                        return CommonResult.success(schedules, "1");
                    }
                }else {
                    return CommonResult.failed("0");
                }
            }
        }
    }

    /**
     * 管理员删除对应id的班次
     * @param id 要删除的班次的id
     * @param page 表示要返回的页码
     * @param number 表示每页的数据条数
     * @param StartTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param EndTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param session 用户session
     * @return 状态码，status=1表示删除成功，status=0表示输入删除失败，
     * status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/manager/delTime")
    public CommonResult RemoveTime(int id, int page, int number, String StartTime, String EndTime, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("3");
        }else {
            User user = userService.getUser((String) LoginUser);
            LocalDateTime startTime = LocalDateTime.parse(StartTime,df);
            LocalDateTime endTime = LocalDateTime.parse(EndTime,df);

            int permission = userService.getUserpermission(user.getUserId());

            if (permission == 1){
                return CommonResult.failed("2");
            }else {
                if (scheduleService.removeSchcedule(id)){
                    if (StartTime != "" && EndTime != "") {
                        if (startTime.compareTo(endTime) < 0) {
                            return CommonResult.failed("0");
                        }
                        List<Schedule> schedules = scheduleService.getSchedule(page,number);

                        return CommonResult.success(schedules, "1");
                    }else {
                        List<Schedule> schedules = scheduleService.findSchedule(startTime,endTime,page,number);

                        return CommonResult.success(schedules, "1");
                    }
                }else {
                    return CommonResult.failed("0");
                }
            }
        }
    }

    /**
     * 管理员查看用户列表
     * @param info 用户id或用户名，info=”“表示查询所有用户
     * @param page 页码数
     * @param number 一页大小
     * @param session 用户session
     * @return 状态码，status=1表示获取成功，status=0表示输入获取失败，
     * status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/manager/searchUsers")
    public CommonResult GetUserList(Object info, int page, int number, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("3");
        }else {
            User user = userService.getUser((String) LoginUser);

            int permission = userService.getUserpermission(user.getUserId());

            if (permission == 1){
                return CommonResult.failed("2");
            }else {
                if (info == ""){
                    List<User> users = adminService.getUserList(page, number);

                    return CommonResult.success(users,"1");
                }
                User user1 = null,user2 = null;
                user1 = adminService.searchUser((String) info);
                user2 = adminService.searchUser((int) info);

                if (user1 != null){
                    return CommonResult.success(user1,"1");
                }else if (user2 != null){
                    return CommonResult.success(user2,"1");
                }else {
                    return CommonResult.failed("0");
                }
            }
        }
    }

    /**
     * 管理员修改用户权限
     * @param id 要修该的用户id
     * @param flag true代表要将权限修改为管理员，false代表要将权限修改为普通用户
     * @param session 用户session
     * @return 状态码，status=1表示修改成功，status=0表示不能对自己进行修改
     * status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/manager/setPower")
    public CommonResult SetPermission(int id, boolean flag, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("3");
        }else {
            User user = userService.getUser((String) LoginUser);

            int permission = userService.getUserpermission(user.getUserId());

            if (permission == 1){
                return CommonResult.failed("2");
            }else {
                if (flag == true){
                    if (adminService.setUserAdmin(id)){
                        return CommonResult.success("1");
                    }else {
                        return CommonResult.failed("0");
                    }
                }else {
                    if (id == user.getUserId()){
                        return CommonResult.failed("0");
                    }
                    if (adminService.setUser(id, user.getName())){
                        return CommonResult.success("1");
                    }else {
                        return CommonResult.failed("0");
                    }
                }
            }
        }
    }

    /**
     * 管理员修改用户状态
     * @param id 要修该的用户id
     * @param flag true代表要将状态修改为封禁中，false代表要将状态修改为正常
     * @param session 用户session
     * @return 状态码，status=1表示修改成功，status=0表示不能对自己进行修改
     * status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/manager/setStatus")
    public CommonResult SetStatus(int id, boolean flag, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("3");
        }else {
            User user = userService.getUser((String) LoginUser);

            int permission = userService.getUserpermission(user.getUserId());

            if (permission == 1){
                return CommonResult.failed("2");
            }else {
                if (id == user.getUserId()){
                    return CommonResult.failed("0");
                }
                if (flag == true){
                    if (adminService.removeUser(id)){
                        return CommonResult.success("1");
                    }else {
                        return CommonResult.failed("0");
                    }
                }else {
                    if (adminService.recoverUser(id)){
                        return CommonResult.success("1");
                    }else {
                        return CommonResult.failed("0");
                    }
                }
            }
        }
    }

    /**
     * 管理搜索公告
     * @param info 公告标题或id，info=”“表示查询所有公告
     * @param page 页码数
     * @param number 一页大小
     * @param session 用户session
     * @return 状态码，status=1表示获取成功，status=0表示获取失败，
     * status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/manager/searchNotices")
    public CommonResult SearchNotice(Object info, int page, int number, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("3");
        }else {
            User user = userService.getUser((String) LoginUser);

            int permission = userService.getUserpermission(user.getUserId());

            if (permission == 1){
                return CommonResult.failed("2");
            }else {
                if (info == ""){
                    List<Notice> notices = noticeService.getNoticeList(page, number);

                    return CommonResult.success(notices,"1");
                }else {
                    Notice notice1 = null;
                    List<Notice> notice2 = null;
                    notice1 = noticeService.searchNotice((int) info);
                    notice2 = noticeService.searchNotice((String) info, page, number);
                    if (notice1 != null){
                        return CommonResult.success(notice1,"1");
                    }else if (notice2 != null){
                        return CommonResult.success(notice2,"1");
                    }else {
                        return CommonResult.failed("0");
                    }
                }
            }
        }
    }

    /**
     * 管理员删除公告
     * @param id 要删除公告的id
     * @param page 要返回的页面的页码
     * @param number 一页的数据条数
     * @param session 用户的session
     * @return 状态码，status=1表示删除成功，status=0表示删除失败，
     * status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/manager/delNotice")
    public CommonResult RemoveNotice(int id, int page, int number, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("3");
        }else {
            User user = userService.getUser((String) LoginUser);

            int permission = userService.getUserpermission(user.getUserId());

            if (permission == 1){
                return CommonResult.failed("2");
            }else {
                if (noticeService.removeNotice(id)){
                    List<Notice> notices = noticeService.getNoticeList(page, number);

                    return CommonResult.success(notices,"1");
                }else {
                    return CommonResult.failed("0");
                }
            }
        }
    }

    /**
     * 管理员发布公告
     * @param title 公告的标题
     * @param content 公告的内容
     * @param sendTime 发送的时间
     * @param page 页码数
     * @param number 一页的数目
     * @param session 用户的session
     * @return 状态码，status=1表示添加成功，status=0表示添加失败，
     * status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/manager/addNotice")
    public CommonResult addNotice(String title, String content, String sendTime,
                                  int page, int number, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("3");
        }else {
            User user = userService.getUser((String) LoginUser);

            int permission = userService.getUserpermission(user.getUserId());

            if (permission == 1){
                return CommonResult.failed("2");
            }else {
                LocalDateTime SendTime = LocalDateTime.parse(sendTime,df);

                Notice notice = new Notice();
                notice.setTitle(title);
                notice.setContent(content);
                notice.setCreateTime(SendTime);

                if (noticeService.addNotice(notice)){
                    List<Notice> notices = noticeService.getNoticeList(page, number);

                    return CommonResult.success(notices,"1");
                }else {
                    return CommonResult.failed("0");
                }
            }
        }
    }
}

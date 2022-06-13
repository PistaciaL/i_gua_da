package org.nwpu.i_gua_da.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Notice;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.Station;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * 管理员操作
 */
@RequestMapping("")
@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;
    @Autowired

    private ReserveService reserveService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private StationService stationService;

    DateTimeFormatter dfIn = DateTimeFormatter.ofPattern("yyyy/M/d HH", Locale.CHINA);
    DateTimeFormatter dfAddSchedule = DateTimeFormatter.ofPattern("yyyy/M/d HH:mm", Locale.CHINA);
    DateTimeFormatter dfOut = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.CHINA);

    /**
     * 用户查看所有校车班车
     * @param page 表示要返回的页码
     * @param number 表示每页的数据条数
     * @param StartTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param EndTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param session 用户session
     * @return status=1表示查询成功，status=0表示输入的日期格式错误或起始日期大于结束日期等，status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/searchTimes")
    public String AdminSearchTime(@RequestParam("page") int page, @RequestParam("number") int number,
                                        @RequestParam(value = "startTime", defaultValue = "2022/01/01 00") String StartTime,
                                        @RequestParam(value = "endTime", required = false) String EndTime, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return "{\"status\":3}";
        }else {
            User user = adminService.searchUser(userId);
            int permission = userService.getUserpermission(user.getUserId());
            if (permission == 1)
                return "{\"status\":2}";
            Page<Schedule> schedules = null;
            try {
                LocalDateTime startTime = null;
                LocalDateTime endTime = null;
                if(EndTime == null || "".equals(EndTime))
                    endTime = LocalDateTime.of(2099, 01, 01, 00, 00);
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

    /**
     * 管理员增添班次
     * @param page 表示要返回的页码
     * @param number 表示每页的数据条数
     * @param startTimeStr 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param endTimeStr 格式为”yyyy/mm/dd“的字符串，表示时间
     * @param departureTimeStr 要添加的班次的出发时间
     * @param startStationName 要添加的班次的始发站
     * @param endStationName 要添加的班次的终点站
     * @param session 用户session
     * @return 状态码，status=1表示添加成功，status=0表示输入数据有误，如起点站和终点站一样，发车时间在现在之前，
     * status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/addTime")
    public String addTime(@RequestParam("page") int page, @RequestParam("number") int number,
                                @RequestParam(value = "startTime", defaultValue = "2022/01/01 00") String startTimeStr,
                                @RequestParam(value = "endTime", required = false) String endTimeStr,
                                @RequestParam("time") String departureTimeStr,
                                @RequestParam("start") String startStationName, @RequestParam("end") String endStationName, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null)
            return "{\"status\":3}";
        User user = adminService.searchUser(userId);
        int permission = userService.getUserpermission(user.getUserId());
        if (permission == 1)
            return "{\"status\":2}";
        if(departureTimeStr == null)
            return "{\"status\":0}";
        LocalDateTime departureTime = null;
        try {
            departureTime = LocalDateTime.parse(departureTimeStr, dfAddSchedule);
        } catch (DateTimeParseException e) {
            return "{\"status\":0}";
        }
        if(departureTime.isBefore(LocalDateTime.now()))
            return "{\"status\":0}";
        Schedule newSchedule = new Schedule();
        if(startStationName == null || endStationName == null || "".equals(startStationName) || "".equals(endStationName))
            return "{\"status\":0}";
        Station startStation = stationService.selectStationByStationName(startStationName);
        Station endStation = stationService.selectStationByStationName(endStationName);
        newSchedule.setStartStation(startStation);
        newSchedule.setEndStation(endStation);
        newSchedule.setDepartureTime(departureTime);
        if(scheduleService.addSchcedule(newSchedule)) {
            //与"/manager/searchTimes"相同
            Page<Schedule> schedules = null;
            try {
                LocalDateTime startTime = null;
                LocalDateTime endTime = null;
                if(endTimeStr == null || "".equals(endTimeStr))
                    endTime = LocalDateTime.of(2099, 01, 01, 00, 00);
                else {
                    try {
                        endTime = LocalDateTime.parse(endTimeStr,dfIn);
                    } catch (DateTimeParseException e) {
                        endTimeStr += " 00";
                        endTime = LocalDateTime.parse(endTimeStr,dfIn);
                    }
                }
                try {
                    startTime = LocalDateTime.parse(startTimeStr,dfIn);
                } catch (DateTimeParseException e) {
                    startTimeStr += " 00";
                    startTime = LocalDateTime.parse(startTimeStr, dfIn);
                }
                schedules = (Page) scheduleService.findSchedule(startTime, endTime, page, number);
            } catch (NullPointerException e) {
                return "{\"status\":1}";
            } catch (IllegalArgumentException e) {
                return "{\"status\":1}";
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
        } else
            return "{\"status\":0}";
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
    @RequestMapping("/delTime")
    public String RemoveTime(@RequestParam("id") int id, @RequestParam("page") int page, @RequestParam("number")int number,
                             @RequestParam(value = "startTime", defaultValue = "2022/01/01 00") String StartTime,
                             @RequestParam("endTime") String EndTime, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return "{\"status\":3}";
        }
        User user = adminService.searchUser(userId);
        int permission = userService.getUserpermission(user.getUserId());
        if (permission == 1)
            return "{\"status\":2}";
        if(scheduleService.removeSchcedule(id)) {
            //调用"/manager/searchTimes"并修改部分status返回值
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
                return "{\"status\":1}";
            } catch (IllegalArgumentException e) {
                return "{\"status\":1}";
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
        } else {
            return "{\"status\":0}";
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
    @RequestMapping("/searchUsers")
    public String GetUserList(@RequestParam("info") String info, @RequestParam("page") int page, @RequestParam("number") int number, HttpSession session){
        Integer loginUserId = (Integer) session.getAttribute("userId");
        if (loginUserId == null){
            return "{\"status\":3}";
        }
        User loginUser = adminService.searchUser(loginUserId);
        int permission = userService.getUserpermission(loginUser.getUserId());
        if (permission == 1)
            return "{\"status\":2}";
        StringJoiner mainSj = new StringJoiner(",", "{", "}");
        Page<User> users = null;
        if(info == null || "".equals(info)) {
            users = (Page) adminService.getUserList(page, number);
            if(users == null) {
                mainSj.add("\"status\":1");
                mainSj.add("\"currentPage\":"+1);
                mainSj.add("\"pages\":"+1);
                return mainSj.toString();
            }
        } else {
            int userId = -1;
            String userNameLike = null;
            try {
                userId = Integer.parseInt(info);
            } catch (NumberFormatException e) {
                userNameLike = info;
            }
            if(userId == -1) {
                //匹配用户名搜索
                users = (Page) adminService.listUserByLikeUserName(userNameLike, page, number);
            } else {
                //匹配用户id搜索
                User user = adminService.searchUser(userId);
                List<User> list = new Page<>();
                list.add(user);
                PageInfo<User> pageInfo = new PageInfo<>(list);
                users = (Page) pageInfo.getList();
            }
        }
        if(users == null)
            return "{\"status\":0}";
        mainSj.add("\"status\":1");
        mainSj.add("\"currentPage\":"+users.getPageNum());
        mainSj.add("\"pages\":"+users.getPages());
        StringJoiner sj = new StringJoiner(",", "\"users\": [", "]");
        for(User user : users) {
            StringJoiner sj1 = new StringJoiner(",", "{", "}");
            sj1.add("\"id\":"+user.getUserId());
            sj1.add("\"userName\":\""+user.getName()+"\"");
            sj1.add("\"password\":\""+user.getPassword()+"\"");
            sj1.add("\"email\":\""+user.getEmail()+"\"");
            String statusStr = null;
            if(user.getStatus() == 1)
                statusStr = "正常";
            else
                statusStr = "封禁";
            sj1.add("\"status\":\""+statusStr+"\"");
            String permissionStr = null;
            if(user.getPermission() == 1)
                permissionStr = "普通用户";
            else
                permissionStr = "管理员";
            sj1.add("\"power\":\""+permissionStr+"\"");
            sj1.add("\"userId\":\""+ user.getStudentNumber() +"\"");
            sj.add(sj1.toString());
        }
        mainSj.add(sj.toString());
        return mainSj.toString();
    }

    /**
     * 管理员修改用户权限
     * @param id 要修该的用户id
     * @param permission true代表要将权限修改为管理员，false代表要将权限修改为普通用户
     * @param session 用户session
     * @return 状态码，status=1表示修改成功，status=0表示不能对自己进行修改
     * status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/setPower")
    public String SetPermission(@RequestParam("id") int id, @RequestParam("power") boolean permission, HttpSession session){
        Integer loginUserId = (Integer) session.getAttribute("userId");
        if (loginUserId == null){
            return "{\"status\":3}";
        }
        User loginUser = adminService.searchUser(loginUserId);
        int ownPermission = userService.getUserpermission(loginUser.getUserId());
        if (ownPermission == 1)
            return "{\"status\":2}";
        if(loginUserId == id)
            return "{\"status\":0}";
        if(permission) {
            if (adminService.setUserAdmin(id)) {
                return "{\"status\":1}";
            } else {
                //修改失败时(一般不会触发)
                return "{\"status\":3}";
            }
        } else {
            //传递的status不为2时, 则修改为普通用户
            if(adminService.setUser(id, loginUser.getName())) {
                return "{\"status\":1}";
            } else {
                return "{\"status\":0}";
            }
        }
    }

    /**
     * 管理员修改用户状态
     * @param id 要修该的用户id
     * @param status true代表要将状态修改为封禁中，false代表要将状态修改为正常
     * @param session 用户session
     * @return 状态码，status=1表示修改成功，status=0表示不能对自己进行修改
     * status=2表示用户无管理员权限，status=3表示用户session失效
     */
    @RequestMapping("/setStatus")
    public String SetStatus(@RequestParam("id") int id, @RequestParam("status") boolean status, HttpSession session){
        Integer loginUserId = (Integer) session.getAttribute("userId");
        if (loginUserId == null){
            return "{\"status\":3}";
        }
        User loginUser = adminService.searchUser(loginUserId);
        int permission = userService.getUserpermission(loginUser.getUserId());
        if (permission == 1)
            return "{\"status\":2}";
        if(loginUserId == id)
            return "{\"status\":0}";
        if(status) {
            adminService.removeUser(id);
        } else {
            adminService.recoverUser(id);
        }
        return "{\"status\":1}";
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
    @RequestMapping("/searchNotices")
    public String SearchNotice(@RequestParam("info") String info, @RequestParam("page") int page, @RequestParam("number") int number, HttpSession session){
        Integer loginUserId = (Integer) session.getAttribute("userId");
        if (loginUserId == null){
            return "{\"status\":3}";
        }
        User loginUser = adminService.searchUser(loginUserId);
        int permission = userService.getUserpermission(loginUser.getUserId());
        if (permission == 1)
            return "{\"status\":2}";
        StringJoiner mainSj = new StringJoiner(",", "{", "}");
        Page<Notice> notices = null;
        if(info == null || "".equals(info)) {
            notices = (Page) noticeService.getNoticeList(page, number);
            if(notices == null) {
                mainSj.add("\"status\":1");
                mainSj.add("\"currentPage\":"+1);
                mainSj.add("\"pages\":"+1);
                return mainSj.toString();
            }
        } else {
            int noticeId = -1;
            String noticeTitleLike = null;
            try {
                noticeId = Integer.parseInt(info);
            } catch (NumberFormatException e) {
                noticeTitleLike = info;
            }
            if(noticeId == -1) {
                //匹配标题搜索
                notices = (Page) adminService.listUserByLikeUserName(noticeTitleLike, page, number);
            } else {
                //匹配公告id搜索
                Notice notice = noticeService.searchNotice(noticeId);
                List<Notice> list = new Page<>();
                list.add(notice);
                PageInfo<Notice> pageInfo = new PageInfo<>(list);
                notices = (Page) pageInfo.getList();
            }
        }
        if(notices == null)
            return "{\"status\":0}";
        mainSj.add("\"status\":1");
        mainSj.add("\"currentPage\":"+notices.getPageNum());
        mainSj.add("\"pages\":"+notices.getPages());
        StringJoiner sj = new StringJoiner(",", "\"notices\": [", "]");
        for(Notice notice : notices) {
            StringJoiner sj1 = new StringJoiner(",", "{", "}");
            sj1.add("\"id\":"+notice.getNoticeId());
            sj1.add("\"title\":\""+notice.getTitle()+"\"");
            sj1.add("\"content\":\""+notice.getContent()+"\"");
            sj1.add("\"sender\":\""+notice.getSender().getName()+"\"");
            sj1.add("\"sendTime\":\""+notice.getCreateTime().format(dfOut)+"\"");
            sj.add(sj1.toString());
        }
        mainSj.add(sj.toString());
        return mainSj.toString();
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
    @RequestMapping("/delNotice")
    public String RemoveNotice(@RequestParam("id") int id, @RequestParam("page") int page, @RequestParam("number") int number, HttpSession session){
        Integer loginUserId = (Integer) session.getAttribute("userId");
        if (loginUserId == null){
            return "{\"status\":3}";
        }
        User loginUser = adminService.searchUser(loginUserId);
        int permission = userService.getUserpermission(loginUser.getUserId());
        if (permission == 1)
            return "{\"status\":2}";
        if(noticeService.removeNotice(id)) {
            //删除成功
            StringJoiner mainSj = new StringJoiner(",", "{", "}");
            Page<Notice> notices = (Page) noticeService.getNoticeList(page, number);
            if(notices == null) {
                mainSj.add("\"status\":1");
                mainSj.add("\"currentPage\":"+1);
                mainSj.add("\"pages\":"+1);
                return mainSj.toString();
            }
            mainSj.add("\"status\":1");
            mainSj.add("\"currentPage\":"+notices.getPageNum());
            mainSj.add("\"pages\":"+notices.getPages());
            StringJoiner sj = new StringJoiner(",", "\"notices\": [", "]");
            for(Notice notice : notices) {
                StringJoiner sj1 = new StringJoiner(",", "{", "}");
                sj1.add("\"id\":"+notice.getNoticeId());
                sj1.add("\"title\":\""+notice.getTitle()+"\"");
                sj1.add("\"content\":\""+notice.getContent()+"\"");
                sj1.add("\"sender\":\""+notice.getSender().getName()+"\"");
                sj1.add("\"sendTime\":\""+notice.getCreateTime().format(dfOut)+"\"");
                sj.add(sj1.toString());
            }
            mainSj.add(sj.toString());
            return mainSj.toString();
        } else {
            return "{\"status\":0}";
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
    @RequestMapping("/addNotice")
    public String addNotice(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("sendTime") String sendTime,
                                  @RequestParam("page") int page, @RequestParam("number") int number, HttpSession session){
        Integer loginUserId = (Integer) session.getAttribute("userId");
        if (loginUserId == null){
            return "{\"status\":3}";
        }
        User loginUser = adminService.searchUser(loginUserId);
        int permission = userService.getUserpermission(loginUser.getUserId());
        if (permission == 1)
            return "{\"status\":2}";
        Notice newNotice = new Notice();
        if(title == null || content == null || "".equals(title) || "".equals(content))
            return "{\"status\":0}";
        newNotice.setTitle(title);
        newNotice.setContent(content);
        newNotice.setSender(loginUser);
        LocalDateTime createTime = null;
        if(createTime == null)
            createTime = LocalDateTime.now();
        else {
            try {
                createTime = LocalDateTime.parse(sendTime, dfIn);
            } catch (DateTimeParseException e) {
                sendTime += " 00";
                createTime = LocalDateTime.parse(sendTime,dfIn);
            }
        }
        if(noticeService.addNotice(newNotice)) {
            //添加成功
            StringJoiner mainSj = new StringJoiner(",", "{", "}");
            Page<Notice> notices = (Page) noticeService.getNoticeList(page, number);
            if(notices == null) {
                mainSj.add("\"status\":1");
                mainSj.add("\"currentPage\":"+1);
                mainSj.add("\"pages\":"+1);
                return mainSj.toString();
            }
            mainSj.add("\"status\":1");
            mainSj.add("\"currentPage\":"+notices.getPageNum());
            mainSj.add("\"pages\":"+notices.getPages());
            StringJoiner sj = new StringJoiner(",", "\"notices\": [", "]");
            for(Notice notice : notices) {
                StringJoiner sj1 = new StringJoiner(",", "{", "}");
                sj1.add("\"id\":"+notice.getNoticeId());
                sj1.add("\"title\":\""+notice.getTitle()+"\"");
                sj1.add("\"content\":\""+notice.getContent()+"\"");
                sj1.add("\"sender\":\""+notice.getSender().getName()+"\"");
                sj1.add("\"sendTime\":\""+notice.getCreateTime().format(dfOut)+"\"");
                sj.add(sj1.toString());
            }
            mainSj.add(sj.toString());
            return mainSj.toString();
        } else {
            return "{\"status\":0}";
        }
    }
}

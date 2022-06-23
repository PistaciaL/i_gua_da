package org.nwpu.i_gua_da.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.*;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.nwpu.i_gua_da.fastjson.UserData;
import org.nwpu.i_gua_da.fastjson.UserVo;
import org.nwpu.i_gua_da.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * 管理员操作控制类
 */
@RequestMapping("/manager")
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
    @Autowired
    private MessageService messageService;

    DateTimeFormatter dfOut = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.CHINA);

    /**
     * 增加信誉度接口，当用户答题答对8道以上时触发，每次将信誉度+1
     * 信誉度处于0-10之间，当信誉度为10时添加失败
     * @param userId 用户id
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/incrementCredit")
    public String incrementCredit(@RequestParam("userId")int userId,@RequestParam("code")String code){
        if (userService.incrementCredit(userId)){
            return "{\"status\":200}";
        }else {
            throw new RuntimeException("更新失败");
        }
    }

    /**
     * 管理员扣除用户信誉度接口，每次扣除1点信誉度，当信誉度为0时扣除失败
     * @param userId 用户id
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/decrementCredit")
    public String decrementCredit(@RequestParam("userId")int userId,@RequestParam("code")String code){
        if (userService.decrementCredit(userId)){
            return "{\"status\":200}";
        }else {
            throw new RuntimeException("更新失败");
        }
    }

    /**
     * 管理员增加班次接口
     * @param date 班次出发时间
     * @param startStationId 起始站点id
     * @param endStationId 终点站id
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/addSchedule")
    public String addSchedule(@RequestParam("departureDatetime") String date,
                              @RequestParam("startStationId") int startStationId,
                              @RequestParam("endStationId") int endStationId,
                              @RequestParam("code")String code) {
        LocalDateTime departureTime = LocalDateTime.parse(date, dfOut);
        Schedule newSchedule = new Schedule();
        Station startStation = stationService.selectStationByStationId(startStationId);
        Station endStation = stationService.selectStationByStationId(endStationId);
        newSchedule.setStartStation(startStation);
        newSchedule.setEndStation(endStation);
        newSchedule.setDepartureDateTime(departureTime);
        scheduleService.addSchcedule(newSchedule);
        return "{\"status\":200}";
    }

    /**
     * 管理员删除班次接口
     * @param scheduledId 班次id
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/deleteSchedule")
    public String deleteSchedule(@RequestParam("scheduleId") int scheduledId,
                             @RequestParam("code")String code) {
        //已发车的班次不能删
        Schedule schedule = scheduleService.getScheduleId(scheduledId);
        if (schedule.getDepartureDateTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("班次已发车，无法删除");
        }
        //将预约该班次的预约
        reserveService.removeReserveByScheduleId(scheduledId);
        scheduleService.removeSchcedule(scheduledId);
        return "{\"status\":200}";
    }

    /**
     * 管理员查看用户列表
     * @param info 用户id或用户名，info=”“表示查询所有用户
     * @param page 页码数
     * @param pageSize 一页大小
     * @return
     */
    @RequestMapping("/searchUsers")
    public String GetUserList(@RequestParam("info") String info,
                              @RequestParam("page") int page,
                              @RequestParam("pageSize") int pageSize,
                              @RequestParam("code") String code){
        User selectedUser = userService.getUserByCode(code);
        PageInfo<User> users = null;
        if(StringUtil.isEmpty(info)) {
            //学号为空，默认搜索全部
            users = adminService.getUserList(selectedUser.getUserId(),page,pageSize);
        }else {
            //模糊匹配学号搜索
            users = adminService.listUserByLikeStudentNumber(selectedUser.getUserId(), Integer.parseInt(info), page, pageSize);
        }
        //返回参数封装，隐藏openid
        UserData result = new UserData();
        result.setStatus(200);
        result.setPage(page);
        result.setTotalPageNumb(users.getPages());
        List<UserVo> userVos = new ArrayList<>();
        for (User user : users.getList()) {
            UserVo userVo = new UserVo();
            userVo.setUserId(user.getUserId());
            userVo.setNickname(user.getName());
            userVo.setStatus(user.getStatus());
            userVo.setEmail(user.getEmail());
            userVo.setPermission(user.getPermission());
            userVo.setStudentNumber(user.getStudentNumber());
            userVo.setCredit(user.getCredit());
            userVos.add(userVo);
        }
        result.setData(userVos);
        return JSON.toJSONString(result);
    }

    /**
     * 设置用户权限接口
     * @param userId 待设置用户的id
     * @param permission 用户权限,1代表普通用户,2代表管理员用户
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/setPermission")
    public String SetPermission(@RequestParam("userId") int userId,
                                @RequestParam("newPermission") int permission,
                                @RequestParam("code") String code){
        //必要的参数校验
        if (userId<0||permission<1||permission>2){
            throw new IllegalArgumentException();
        }
        if (!adminService.setUserPermission(userId,permission)){
            throw new RuntimeException("更新失败");
        }
        return "{\"status\":200}";
    }

    /**
     * 封禁/解封用户接口(已废弃)
     * @param userId 待设置用户的id
     * @param status 用户新状态,1代表正常,2代表封禁
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/setStatus")
    public String SetStatus(@RequestParam("userId") int userId,
                            @RequestParam("newStatus") int status,
                            @RequestParam("code")String code){
        //必要的参数校验
        if (userId<1||status<1||status>2){
            throw new IllegalArgumentException();
        }
        //根据参数决定恢复用户还是封禁用户
        if (status==1&&!adminService.recoverUser(userId)){
            throw new RuntimeException("更新失败");
        }else if (status==2&&!adminService.removeUser(userId)){
            throw new RuntimeException("更新失败");
        }
        return "{\"status\":200}";
    }

    /**
     * 管理员发布公告
     * @param title 公告的标题
     * @param content 公告的内容
     * @param code 微信小程序状态码
     * @return 状态码，status=200表示添加成功，status=420表示添加失败，
     * status=404表示用户无管理员权限，status=403表示用户未登录
     */
    @RequestMapping("/addNotice")
    public String addNotice(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("code") String code){
        /*
        权限处理，预留
         */
        User user = userService.getUserByCode(code);
        if (user == null || user.getStatus() == 2)
            return "{\"status\":403}";
        if (user.getPermission() != 2)
            return "{\"status\":404}";

        Notice newNotice = new Notice();

        if(title == null || content == null || "".equals(title) || "".equals(content))
            return "{\"status\":420}";
        newNotice.setTitle(title);
        newNotice.setContent(content);
        newNotice.setSender(user);
        LocalDateTime createTime = null;
        createTime = LocalDateTime.now();
        newNotice.setCreateTime(createTime);
        try {
            if(noticeService.addNotice(newNotice)) {
                return "{\"status\":200}";
            }else {
                return "{\"status\":420}";
            }
        } catch (Exception e) {
            return "{\"status\":420}";
        }
    }

    /**
     * 管理员删除公告
     * @param id 要删除公告的id
     * @param code 微信小程序状态码
     * @return 状态码，status=200表示删除成功，status=420表示删除失败，
     * status=404表示用户无管理员权限，status=403表示用户未登录
     */
    @RequestMapping("/deleteNotice")
    public String RemoveNotice(@RequestParam("noticeId") int id,
                               @RequestParam("code") String code){
        try {
            if(noticeService.removeNotice(id)) {
                return "{\"status\":200}";
            }else {
                return "{\"status\":420}";
            }
        } catch (Exception e) {
            return "{\"status\":420}";
        }
    }

    /**
     * 管理搜索公告
     * @param info 公告标题或id，info=”“表示查询所有公告
     * @param page 页码数
     * @param pageSize 一页大小
     * @param code 微信小程序状态码
     * @return 状态码，status=200表示获取成功，status=420表示获取失败，
     * status=404表示用户无管理员权限，status=403表示用户未登录
     */
    @RequestMapping("/searchNotices")
    public String SearchNotice(@RequestParam("info") String info,
                               @RequestParam("page") int page,
                               @RequestParam("pageSize") int pageSize,
                               @RequestParam("code") String code){
        StringJoiner mainSj = new StringJoiner(",", "{", "}");
        int judge = 0;
        Map<String,Object> result = new HashMap<>();
        Page<Notice> notices = null;
        if(info == null || "".equals(info)) {
            PageInfo<Notice> pageInfo = noticeService.getNoticeList(page,pageSize);
            notices = (Page) pageInfo.getList();
            if(notices == null) {
                result.put("status",200);
                result.put("page",pageInfo.getPageNum());
                result.put("totalPageNumb",pageInfo.getPages());
                return JSON.toJSONString(result);
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
                PageInfo<Notice> pageInfo = noticeService.listNoticeByNoticeTitleLike(noticeTitleLike, page, pageSize);
                List<Notice> noticeList = pageInfo.getList();
                //匹配标题搜索
                if (noticeList != null) {
                    notices = (Page)noticeList;
                } else {
                    judge = 1;
                }
            } else {
                //匹配公告id搜索
                Notice notice = noticeService.searchNotice(noticeId);
                if (notice != null) {
                    List<Notice> list = new Page<>();
                    list.add(notice);
                    PageInfo<Notice> pageInfo = new PageInfo<>(list);
                    notices = (Page) pageInfo.getList();
                } else {
                    judge = 1;
                }
            }
        }
        if(judge == 1)
            return "{\"status\":420}";
        mainSj.add("\"status\":200");
        mainSj.add("\"page\":"+notices.getPageNum());
        mainSj.add("\"totalPageNumb\":"+notices.getPages());
        StringJoiner sj = new StringJoiner(",", "\"data\": [", "]");
        for(Notice notice : notices) {
            StringJoiner sj1 = new StringJoiner(",", "{", "}");
            sj1.add("\"noticeId\":"+notice.getNoticeId());
            sj1.add("\"title\":\""+notice.getTitle()+"\"");
            sj1.add("\"content\":\""+notice.getContent()+"\"");
            sj1.add("\"createTime\":\""+notice.getCreateTime().format(dfOut)+"\"");
            sj1.add("\"senderStudentNumber\":\""+notice.getSender().getStudentNumber()+"\"");
            sj.add(sj1.toString());
        }
        mainSj.add(sj.toString());
        return mainSj.toString();
    }

    /**
     * 获取特定类型留言接口
     * @param type 留言类型，1代表失物寻主，2代表寻物启事，3代表乘车意见，4代表其他
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/getMessageByType")
    public String GetMessageByType(@RequestParam("type")int type,
                                   @RequestParam("code")String code){

        List<Message> messageInfo = messageService.listMessageByType(type);
        if(messageInfo==null){
            return "{\"status\":200,\"data\":[]}";
        }

        List<Map<String,Object>> data = new ArrayList<>();

        Map<String,Object> result = new HashMap<>();
        result.put("status",200);
        List<Message> messages = messageInfo;
        for (Message message : messages) {
            Map<String, Object> map = new HashMap<>();
            map.put("messageId",message.getMessageId());
            map.put("content",message.getContent());
            map.put("type",message.getType());
            data.add(map);
        }
        result.put("data",data);
        return JSON.toJSONString(result);
    }
}

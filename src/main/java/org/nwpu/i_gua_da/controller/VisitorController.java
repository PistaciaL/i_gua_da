package org.nwpu.i_gua_da.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.nwpu.i_gua_da.entity.Notice;
import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.fastjson.UserVo;
import org.nwpu.i_gua_da.service.NoticeService;
import org.nwpu.i_gua_da.service.ScheduleService;
import org.nwpu.i_gua_da.service.UserService;
import org.nwpu.i_gua_da.service.UserloginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Value("${redisKey.verificationCode}")
    private String codeKey;

    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping("/login")
    public String Login(@RequestParam("code") String code){
        //前置拦截器会处理登录和权限逻辑
        User user = userService.getUserByCode(code);
        user.setOpenid("");
        user.setStatus(200);
        System.out.println(JSON.toJSONString(user));
        return JSON.toJSONString(user);
    }


    @RequestMapping("/register")
    public String Register(@RequestParam("nickname") String nickname,
                           @RequestParam("studentNumber")String studentNumber,
                           @RequestParam("email")String email,
                           @RequestParam("verificationCode")String verificationCode,
                           @RequestParam("code")String code){
        if (StringUtil.isEmpty(nickname)||StringUtil.isEmpty(studentNumber)
                ||StringUtil.isEmpty(email)||StringUtil.isEmpty(verificationCode)){
            throw new RuntimeException("参数异常");
        }
        User user = userService.getUserByCode(code);
        if (user==null){
            throw new RuntimeException("请先登录");
        }
        String redisVerificationCode = stringRedisTemplate.opsForValue().
                get(codeKey + ":" + user.getUserId());
        if (redisVerificationCode==null){
            throw new RuntimeException("请先获取验证码");
        }
        if (!redisVerificationCode.equals(verificationCode)){
            throw new RuntimeException("验证码错误");
        }
        user.setName(nickname);
        user.setEmail(email);
        user.setStudentNumber(studentNumber);
        if (user.getPassword()==null){
            user.setPassword("123456");
        }
        if (!userService.updateUserByOpenid(user.getOpenid(),nickname,studentNumber,email)){
            throw new RuntimeException("注册失败");
        }
        user.setOpenid("");
        //这里偷懒借用user的status存放响应码
        user.setStatus(200);
        System.out.println(user.toString());
        return JSON.toJSONString(user);
    }
}

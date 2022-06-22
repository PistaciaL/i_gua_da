package org.nwpu.i_gua_da.controller;

import com.alibaba.fastjson.JSON;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.fastjson.UserVo;
import org.nwpu.i_gua_da.service.AdminService;
import org.nwpu.i_gua_da.service.UserService;
import org.nwpu.i_gua_da.service.UserloginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/updateInfo")
    public String updateInfo(@RequestParam("nickname") String newUserName, @RequestParam("studentNumber")String newStudentNumber,
                            @RequestParam("email") String newEmail,@RequestParam("code") String code){
        User user = userService.getUserByCode(code);
        user.setName(newUserName);
        user.setStudentNumber(newStudentNumber);
        user.setEmail(newEmail);
        if (userService.setUserInformation(user)){
            return "{\"status\":200}";
        }else {
            throw new RuntimeException("修改失败");
        }
    }


    @RequestMapping("/getInfo")
    public String getInfo(@RequestParam("code")String code){
        User user = userService.getUserByCode(code);
        if (user==null){
            throw new RuntimeException("用户不存在");
        }
        UserVo userVo = new UserVo();
        userVo.setUserId(user.getUserId());
        userVo.setNickname(user.getName());
        userVo.setStatus(user.getStatus());
        userVo.setEmail(user.getEmail());
        userVo.setStudentNumber(user.getStudentNumber());
        userVo.setPermission(user.getPermission());
        return JSON.toJSONString(userVo);
    }
}

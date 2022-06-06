package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.api.CommonResult;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.UserloginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户注册
 */
@RestController
public class RegisterController {

    @Autowired
    private UserloginService userloginService;

    /**
     * 用户注册
     * @param username 用户名
     * @param password 用户密码
     * @param email 用户邮箱
     * @param userId 用户id
     * @return status=1表示注册成功，status=0表示注册失败(一个邮箱只能注册一次)
     */
    @RequestMapping("/register")
    public CommonResult Register(String username, String password, String email, Integer userId){
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setUserId(userId);

        if (userloginService.addUser(user)){

            return CommonResult.success("1");
        }else {
            return CommonResult.failed("0");
        }
    }
}

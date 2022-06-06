package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.api.CommonResult;
import org.nwpu.i_gua_da.api.ResultCode;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.UserService;
import org.nwpu.i_gua_da.service.UserloginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 用户登录
 */
@RestController
public class LoginController {

    @Autowired
    private UserloginService userloginService;
    @Autowired
    private UserService userService;

    /**
     * 用户登录方法
     * @param username 用户名
     * @param password 密码
     * @return status=1代表登录成功，status=0代表账户或密码错误，status=2代表该账户被封禁，禁止登录
     * 成功登录的用户的部分信息
     */
    @RequestMapping("/login")
    public CommonResult Login(String username, String password, HttpSession session){
        int status = 1;
        User user = new User(username,password);
        if (userloginService.verifyUser(user)){
            User user1 = userService.getUser(username);
            int i = userService.getUserStatus(user1);
            if (i == 2){
                return CommonResult.failed("2");
            }else {
                session.setAttribute("LoginUser",username);
                status = 1;
                int permission = userService.getUserpermission(user1.getUserId());
                boolean flag = false;
                if (permission == 2){
                    flag = true;
                }
                String userinfo = user1.getName() + "," + user1.getEmail() + "," + user1.getUserId() + flag;
                return CommonResult.success(userinfo, "1");
            }
        }else {
            return CommonResult.failed("0");
        }
    }
}

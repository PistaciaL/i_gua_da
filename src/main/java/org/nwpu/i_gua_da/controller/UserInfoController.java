package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.api.CommonResult;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 用户信息修改
 */
@RestController
public class UserInfoController {

    @Autowired
    private UserService userService;

    /**
     * 用户修改信息
     * （一个用户只能被一个用户使用，一个邮箱只能被一个用户使用）
     * @param newUserName 新用户名
     * @param newPassword 新密码
     * @param newEmail 新邮箱
     * @return status = 1代表修改成功，status=0代表邮箱或用户名已经被别人使用过，status=2代表用户session失效
     */
    @RequestMapping("/user/resetInfo")
    public CommonResult resetInfo(String newUserName, String newPassword, String newEmail, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("2");
        } else {
            User user = userService.getUser((String) LoginUser);
            user.setName(newUserName);
            user.setPassword(newPassword);
            user.setEmail(newEmail);
            int i = userService.setUserInformation(user);
            if (i == 1){
                return CommonResult.success("1");
            }else {
                return CommonResult.failed("0");
            }
        }
    }

    /**
     * 用户修改密码
     * @param Password 用户原密码
     * @param newPassword 用户新密码
     * @param session 用户session
     * @return status = 1代表修改成功，status=0代表原密码错误，status=2代表用户session失效
     */
    @RequestMapping("/user/resetPassword")
    public CommonResult resetPassword(String Password, String newPassword, HttpSession session){
        Object LoginUser = session.getAttribute("LoginUser");
        if (LoginUser == null){
            return CommonResult.failed("2");
        }else {
            User user = userService.getUser((String)LoginUser);
            if (Password.equals(user.getPassword())){
                user.setPassword(newPassword);
                if (userService.setUserpassword(user)){
                    return CommonResult.success("1");
                }else {
                    return CommonResult.failed("0");
                }
            }else {
                return CommonResult.failed("0");
            }
        }
    }
}

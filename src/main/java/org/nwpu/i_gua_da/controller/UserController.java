package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.AdminService;
import org.nwpu.i_gua_da.service.UserService;
import org.nwpu.i_gua_da.service.UserloginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.StringJoiner;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserloginService userloginService;
    @Autowired
    private AdminService adminService;

    /**
     * 用户修改信息
     * （一个用户只能被一个用户使用，一个邮箱只能被一个用户使用）
     * @param newUserName 新用户名
     * @param newStudentNumber 新学号
     * @param newEmail 新邮箱
     * @return status = 1代表修改成功，status=0代表邮箱或用户名已经被别人使用过，status=2代表用户session失效
     */
    @RequestMapping("/resetInfo")
    public String resetInfo(@RequestParam("userName") String newUserName, @RequestParam("userId")String newStudentNumber,
                            @RequestParam("email")String newEmail, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return "{\"status\":2}";
        }else {
            User user = adminService.searchUser(userId);
            user.setName(newUserName);
            user.setStudentNumber(newStudentNumber);
            user.setEmail(newEmail);
            int i = userService.setUserInformation(user);
            System.out.println(i);
            if (i == 1){
                StringJoiner sj = new StringJoiner(",", "{", "}");
                sj.add("\"status\":1");
                sj.add("\"userName\":\""+user.getName()+"\"");
                sj.add("\"userId\":\""+user.getStudentNumber()+"\"");
                sj.add("\"email\":\""+user.getEmail()+"\"");
                return sj.toString();
            }else {
                return "{\"status\":0}";
            }
        }
    }

    /**
     * 用户修改密码
     * @param password 用户原密码
     * @param newPassword 用户新密码
     * @param session 用户session
     * @return status = 1代表修改成功，status=0代表原密码错误，status=2代表用户session失效
     */
    @RequestMapping("/resetPassword")
    public String resetPassword(@RequestParam("oldPassword") String password, @RequestParam("newPassWord") String newPassword, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return "{\"status\":2}";
        }else {
            User user = adminService.searchUser(userId);
            User verifiedUser = new User();
            verifiedUser.setName(user.getName());
            verifiedUser.setPassword(password);
            if(userloginService.verifyUser(verifiedUser)) {
                user.setPassword(newPassword);
                try{
                    if(userService.setUserpassword(user)) {
                        return "{\"status\":1}";
                    }
                    //异常处理(一般不会触发)
                } catch (IllegalArgumentException e) {
                    return "{\"status\":0}";
                } catch (NullPointerException n) {
                    return "{\"status\":0}";
                }
                return "{\"status\":0}";
            } else {
                return "{\"status\":0}";
            }
        }
    }

}

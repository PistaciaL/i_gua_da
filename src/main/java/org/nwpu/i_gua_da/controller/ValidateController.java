package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.UserService;
import org.nwpu.i_gua_da.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证验证码
 */
@RestController
public class ValidateController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserService userService;

    /**
     * 用户验证码验证
     * @param email 用户邮箱
     * @param newpassword 用户新密码
     * @param code 用户验证码
     * @return status=0代表验证码无效，status=1代表修改密码成功，status=3表示该账户已经被封禁，修改失败
     */
    @RequestMapping("/validate")
    public String Vaildate(@RequestParam("email") String email, @RequestParam("password") String newpassword, @RequestParam("checkStr")String code){
        User user = userService.getUser1(email);
        int i = userService.getUserStatus(user);
        user.setPassword(newpassword);
        if (i == 2){
            return "{\"status\":3}";
        }else {
            if (verificationCodeService.verifyCode(user.getUserId(), code)){
                int status = 1;
                if (userService.setUserpassword(user)){
                    return "{\"status\":1}";
                }else {
                    return "{\"status\":0}";
                }
            }else {
                return "{\"status\":0}";
            }
        }
    }
}

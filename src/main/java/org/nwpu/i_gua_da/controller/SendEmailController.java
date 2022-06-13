package org.nwpu.i_gua_da.controller;

import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.UserService;
import org.nwpu.i_gua_da.service.VerificationCodeService;
import org.nwpu.i_gua_da.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

/**
 * 发送验证码
 */
@RestController
public class SendEmailController {

    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private UserService userService;

    /**
     * 发送邮件
     * @param email
     * @return status=0代表数据库中无此邮箱，status=1代表发送成功
     */
    @RequestMapping("/sendEmail")
    public String SendEmail(@Param("email") String email){
        User user = userService.getUser1(email);
        if (user != null){
            String code = verificationCodeService.createVerificationCode(user.getUserId());
            try {
                emailSender.send(email, code, "找回密码");
            } catch (MessagingException e) {
                return "{\"status\":0}";
            }
            return "{\"status\":1}";
        }else {
            return "{\"status\":0}";
        }
    }
}

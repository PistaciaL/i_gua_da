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
 * 邮件发送控制类
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
     * 发送邮件接口
     * @param email 要发送邮件的目的邮箱
     * @return status=0代表数据库中无此邮箱，status=1代表发送成功
     */
    @RequestMapping("/sendEmail")
    public String SendEmail(@Param("email") String email,
                            @Param("code") String code){
        User user = userService.getUserByCode(code);
        if (user != null){
            String verificationCode = verificationCodeService.createVerificationCode(user.getUserId());
            System.out.println("验证码===>"+verificationCode);
            try {
                emailSender.send(email, verificationCode, "找回密码");
            } catch (MessagingException e) {
                return "{\"status\":420}";
            }
            return "{\"status\":200}";
        }else {
            return "{\"status\":420}";
        }
    }
}

package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.api.CommonResult;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.UserService;
import org.nwpu.i_gua_da.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送验证码
 */
@RestController
public class SendEmailController {

    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private UserService userService;

    /**
     * 发送邮件
     * @param email
     * @return status=0代表数据库中无此邮箱，status=1代表发送成功
     */
    @RequestMapping("/sendEmail")
    public CommonResult SendEmail(String email){
        User user = userService.getUser1(email);

        if (user != null){
            String str = verificationCodeService.createVerificationCode(user.getUserId());
            return CommonResult.success("1");
        }else {

            return CommonResult.failed("0");
        }
    }
}

package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.api.CommonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

/**
 * 用户登出
 */
@RestController
public class LogoutController {

    /**
     * 用户登出，清理sission
     * @param session
     * @param sessionStatus
     * @return status=1表示成功退出登录
     */
    @RequestMapping("/logout")
    public CommonResult Logout(HttpSession session, SessionStatus sessionStatus){
        session.invalidate();//让sission失效
        sessionStatus.setComplete();//清空sission
        int statue = 1;
        return CommonResult.success(statue);
    }
}

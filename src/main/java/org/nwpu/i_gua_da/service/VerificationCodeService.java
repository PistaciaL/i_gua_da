package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.User;

import java.util.concurrent.TimeUnit;

/**
 * 验证码操作
 * 负责生成并验证给用户发送的验证码
 */
public interface VerificationCodeService {

    /**
     * 随机生成六位数字验证码, 默认过期时间为30分钟
     * @param userId 用户id
     * @return 六位数字验证码
     */
    String createVerificationCode(int userId);

    /**
     * 随机生成六位数字验证码
     * @param userId 用户id
     * @param keepAliveTime 过期时间
     * @param unit 时间单位
     * @return 六位数字验证码
     */
    String createVerificationCode(int userId, int keepAliveTime, TimeUnit unit);

    /**
     * 验证验证码的正确性
     * @param code 待验证的验证码
     * @param userId 用户id
     * @return true: 验证成功<br/>false: 验证失败
     */
    boolean verifyCode(int userId, String code);
}

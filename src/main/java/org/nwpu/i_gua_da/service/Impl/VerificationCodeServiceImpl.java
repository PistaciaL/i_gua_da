package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    /**
     * 验证码长度
     */
    @Value("${constants.verificationCode.length}")
    private int codeLength;

    @Value("${redisKey.verificationCode}")
    private String codeKey;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public String createVerificationCode(int userId) {
        return createVerificationCode(userId, 30, TimeUnit.MINUTES);
    }

    @Override
    @Transactional
    public String createVerificationCode(int userId, int keepAliveTime, TimeUnit unit) {
        if(userId < 0 || keepAliveTime < 0)
            throw new IllegalArgumentException();
        if(unit == null)
            throw new NullPointerException();
        //生成随机验证码, 格式为6位数字, 不足补0, 如: 001234
        String code = String.format("%0"+codeLength+"d", (int) (Math.random()*(Math.pow(10, codeLength))));
        stringRedisTemplate.opsForValue().set(codeKey+":"+userId, code, keepAliveTime, unit);
        return code;
    }

    @Override
    @Transactional
    public boolean verifyCode(int userId, String code) {
        if(code == null)
            throw new NullPointerException();
        if(userId < 0 || code.length() != codeLength)
            throw new IllegalArgumentException();
        String value = stringRedisTemplate.opsForValue().get(codeKey+":"+userId);
        boolean b = code.equals(value);
        //验证成功, 验证码失效
        if(b)
            stringRedisTemplate.delete(code);
        return b;
    }
}

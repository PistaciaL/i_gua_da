package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.VerificationCodeService;

import java.util.concurrent.TimeUnit;

public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Override
    public String createVerificationCode(User user) {
        return null;
    }

    @Override
    public String createVerificationCode(User user, int keepAliveTime, TimeUnit unit) {
        return null;
    }

    @Override
    public boolean verifyCode(String code, User user) {
        return false;
    }
}

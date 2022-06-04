package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.mapper.UserMapper;
import org.nwpu.i_gua_da.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceImpl implements UserService {

    @Value("${constants.user.length.password}")
    private int userPasswordMaxLength;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Integer setUserInformation(User user) {
        if(user == null || user.getUserId() == null)
            throw new NullPointerException();
        if(user.getUserId() < 0)
            throw new IllegalArgumentException();
        //暂时无法实现
        return 0;
    }

    @Override
    @Transactional
    public boolean setUserpassword(User user) {
        if(user == null || user.getUserId() == null || user.getPassword() == null)
            throw new NullPointerException();
        if(user.getUserId() < 0 || user.getPassword().length() < 0 || user.getPassword().length() > userPasswordMaxLength)
            throw new IllegalArgumentException();
        int i = userMapper.setUserPassword(user);
        return i == 1;
    }

    @Override
    public Integer getUserpermission(String UserName) {
        //暂时无法实现
        return null;
    }
}

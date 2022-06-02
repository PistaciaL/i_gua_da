package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.mapper.UserMapper;
import org.nwpu.i_gua_da.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Value("${constants.user.permission.common}")
    private int commonPermissionCode;
    @Value("${constants.user.permission.admin}")
    private int adminPermissionCode;
    @Value("${constants.user.length.name}")
    private int userNameMaxLength;
    @Value("${constants.user.status.isDelete}")
    private int isDeleteStatus;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean setUserAdmin(Integer userId) {
        if(userId == null)
            throw new NullPointerException();
        if(userId < 0)
            throw new IllegalArgumentException();
        int i = userMapper.setUserAsAdmin(userId, adminPermissionCode);
        return i == 1;
    }

    @Override
    public boolean setUser(Integer userId, String adminName) {
        if(userId == null || adminName == null)
            throw new NullPointerException();
        if(userId < 0 || adminName.length() == 0 || adminName.length() > userNameMaxLength)
            throw new IllegalArgumentException();
        int i = userMapper.setUserAsCommon(userId, adminName, commonPermissionCode);
        return i == 1;
    }

    @Override
    public User searchUser(Integer userId) {
        if(userId == null)
            throw new NullPointerException();
        if(userId < 0)
            throw new IllegalArgumentException();
         return userMapper.selectUserById(userId);
    }

    @Override
    public User searchUser(String userName) {
        if(userName == null)
            throw new NullPointerException();
        if(userName.length() == 0 || userName.length() > userNameMaxLength)
            throw new IllegalArgumentException();
        return userMapper.selectUserByName(userName);
    }

    @Override
    public boolean removeUser(Integer userId) {
        if(userId == null)
            throw new NullPointerException();
        if(userId < 0)
            throw new IllegalArgumentException();
        int i = userMapper.deleteUserById(userId, isDeleteStatus);
        return i == 1;
    }
}

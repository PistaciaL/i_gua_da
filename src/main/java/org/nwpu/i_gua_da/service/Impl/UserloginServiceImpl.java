package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.mapper.UserMapper;
import org.nwpu.i_gua_da.service.UserloginService;
import org.nwpu.i_gua_da.util.FormatValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserloginServiceImpl implements UserloginService {

    @Value("${constants.user.length.name}")
    private int userNameMaxLength;
    @Value("${constants.user.length.password}")
    private int userPasswordMaxLength;
    @Value("${constants.user.length.email}")
    private int userEmailMaxLength;
    @Value("${constants.user.permission.default}")
    private int permissionDefault;
    @Value("${constants.user.status.default}")
    private int statusDefault;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean verifyUser(User user) {
        if(user == null || user.getName() == null || user.getPassword() == null)
            throw new NullPointerException();
        if(user.getName().length() == 0 || user.getName().length() > userNameMaxLength ||
            user.getPassword().length() == 0 || user.getPassword().length() > userPasswordMaxLength)
            throw new IllegalArgumentException();
        if(userMapper.selectForVerify(user) != null)
            return true;
        return false;
    }

    @Override
    @Transactional
    public boolean addUser(User user) {
        if(user == null || user.getName() == null || user.getPassword() == null || user.getEmail() == null)
            throw new NullPointerException();
        if(user.getName().length() == 0 || user.getName().length() > userNameMaxLength ||
                user.getPassword().length() == 0 || user.getPassword().length() > userPasswordMaxLength ||
                user.getEmail().length() == 0 || user.getEmail().length() > userEmailMaxLength || !FormatValidator.isEmail(user.getEmail()))
            throw new IllegalArgumentException();
        if(user.getRegisterDatetime() == null)
            user.setRegisterDatetime(LocalDateTime.now());
        if(user.getPermission() == null)
            user.setPermission(permissionDefault);
        if(user.getStatus() == null)
            user.setStatus(statusDefault);
        if(userMapper.verifyByNameOrStudentNumbOrEmail(user) != null)
            return false;
        int i = userMapper.addUser(user);
        return i == 1;
    }
}

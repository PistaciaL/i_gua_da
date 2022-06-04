package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.mapper.UserMapper;
import org.nwpu.i_gua_da.service.UserloginService;
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
        if(user == null || user.getName() == null || user.getPassword() == null)
            throw new NullPointerException();
        if(user.getName().length() == 0 || user.getName().length() > userNameMaxLength ||
                user.getPassword().length() == 0 || user.getPassword().length() > userPasswordMaxLength)
            throw new IllegalArgumentException();
        if(user.getRegisterDatetime() == null)
            user.setRegisterDatetime(LocalDateTime.now());
        if(userMapper.selectUserByName(user.getName()) == null) {
            int i = userMapper.addUser(user);
            return i == 1;
        }
        return false;
    }
}

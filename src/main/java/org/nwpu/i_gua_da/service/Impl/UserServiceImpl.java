package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.mapper.UserMapper;
import org.nwpu.i_gua_da.service.UserService;
import org.nwpu.i_gua_da.util.FormatValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Value("${constants.user.length.password}")
    private int userPasswordMaxLength;
    @Value("${constants.user.length.name}")
    private int userNameMaxLength;
    @Value("${constants.user.length.email}")
    private int userEmailMaxLength;
    private String emailFormat = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Integer setUserInformation(User user) {
        if(user == null || user.getUserId() == null || user.getName() == null || user.getEmail() == null || user.getStudentNumber() == null)
            throw new NullPointerException();
        if(user.getUserId() < 0 || user.getStudentNumber() < 0 ||
                user.getName().length() == 0 || user.getName().length() > userNameMaxLength ||
                user.getEmail().length() == 0 || user.getEmail().length() > userEmailMaxLength || !FormatValidator.isEmail(user.getEmail()))
            throw new IllegalArgumentException();
        if(userMapper.verifyByNameOrStudentNumbOrEmail(user) != null)
            return 0;
        int i = userMapper.setUserInformation(user);
        return i == 0 ? 0 : 1;
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
    public Integer getUserpermission(Integer userId) {
        if(userId == null)
            throw new NullPointerException();
        if(userId < 0)
            throw new IllegalArgumentException();
        int i = userMapper.getUserStatusByUserId(userId);
        return i;
    }

}

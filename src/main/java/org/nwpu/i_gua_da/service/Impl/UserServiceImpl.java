package org.nwpu.i_gua_da.service.Impl;

import com.github.pagehelper.util.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.mapper.UserMapper;
import org.nwpu.i_gua_da.service.AdminService;
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
    @Value("${constants.user.length.studentNumber}")
    private int studentNumberMaxLength;
    private String emailFormat = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminService adminService;

    @Override
    @Transactional
    public Integer setUserInformation(User user) {
        if(user == null || user.getUserId() == null || user.getName() == null || user.getEmail() == null || user.getStudentNumber() == null)
            throw new NullPointerException();
        if(user.getUserId() < 0 || user.getStudentNumber().length() == 0 || user.getStudentNumber().length() > studentNumberMaxLength ||
                user.getName().length() == 0 || user.getName().length() > userNameMaxLength ||
                user.getEmail().length() == 0 || user.getEmail().length() > userEmailMaxLength || !FormatValidator.isEmail(user.getEmail()))
            throw new IllegalArgumentException();
        if(userMapper.verifyByNameOrEmail(user) != null)
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
        int i = userMapper.getUserPermissionByUserId(userId);
        return i;
    }

    @Override
    public User getUser(String userName) {
        return adminService.searchUser(userName);
    }

    @Override
    public User getUser1(String email) {
        if(email == null)
            throw new NullPointerException();
        if(email.length() > userEmailMaxLength || email.length() == 0 || !FormatValidator.isEmail(email))
            throw new IllegalArgumentException();
        return userMapper.getUserByEmail(email);
    }

    @Override
    public Integer getUserStatus(User user) {
        if(user == null || user.getUserId() == null)
            throw new NullPointerException();
        if(user.getUserId() < 0)
            throw new IllegalArgumentException();
        return userMapper.getUserStatusByUserId(user.getUserId());
    }

    @Override
    public User getUserByCode(String code) {
        return userMapper.getUserByCode(code);
    }

    @Override
    public boolean addUser(User user) {
        return userMapper.addUser(user)==1;
    }

    public boolean updateUserByOpenid(String openid,String nickname,String studentNumber,String email){
        if (StringUtil.isEmpty(openid)||StringUtil.isEmpty(nickname)||StringUtil.isEmpty(studentNumber)||StringUtil.isEmpty(email)){
            throw new IllegalArgumentException();
        }
        return userMapper.updateUserByOpenid(openid,nickname,studentNumber,email)==1;
    }

}

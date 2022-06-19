package org.nwpu.i_gua_da.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.mapper.UserMapper;
import org.nwpu.i_gua_da.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Value("${constants.user.status.notDelete}")
    private int notDeleteStatus;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean setUserAdmin(Integer userId) {
        if(userId == null)
            throw new NullPointerException();
        if(userId < 0)
            throw new IllegalArgumentException();
        int i = userMapper.setUserAsAdmin(userId, adminPermissionCode, notDeleteStatus);
        return i == 1;
    }

    @Override
    public boolean setUser(Integer userId, String adminName) {
        if(userId == null || adminName == null)
            throw new NullPointerException();
        if(userId < 0 || adminName.length() == 0 || adminName.length() > userNameMaxLength)
            throw new IllegalArgumentException();
        int i = userMapper.setUserAsCommon(userId, adminName, commonPermissionCode, notDeleteStatus);
        return i == 1;
    }

    @Override
    public PageInfo<User> getUserList(Integer pageNum, Integer pageSize) {
        if(pageNum == null || pageSize == null)
            throw new NullPointerException();
        if(pageNum < 1 || pageSize < 1)
            throw new IllegalArgumentException();
        PageHelper.startPage(pageNum-1, pageSize);
        List<User> users = userMapper.getAllUser();
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
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
        int i = userMapper.setUserStatusByUserId(userId, isDeleteStatus);
        return i == 1;
    }

    @Override
    public boolean recoverUser(Integer userId) {
        if(userId == null)
            throw new NullPointerException();
        if(userId < 0)
            throw new IllegalArgumentException();
        int i = userMapper.setUserStatusByUserId(userId, notDeleteStatus);
        return i == 1;
    }

    @Override
    public PageInfo<User> listUserByLikeUserName(String userName, Integer pageNum, Integer pageSize) {
        if(userName == null || pageNum == null || pageSize == null)
            throw new NullPointerException();
        if("".equals(userName) || pageNum < 1 || pageSize <= 0)
            throw new IllegalArgumentException();
        PageHelper.startPage(pageNum-1, pageSize);
        List<User> users = userMapper.listUserByLikeUserName(userName);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    @Override
    public PageInfo<User> listUserByLikeStudentNumber(int studentNumber, int page, int pageSize) {
        if (studentNumber<1 || page<1 || pageSize<1){
            throw new IllegalArgumentException();
        }
        PageHelper.startPage(page-1,pageSize);
        List<User> users = userMapper.listUserByLikeStudentNumber(studentNumber);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    @Override
    public boolean setUserPermission(int userId, int permission) {
        if (userId<1||permission<1||permission>2){
            throw new IllegalArgumentException();
        }
        return userMapper.setUserPermission(userId,permission)==1;
    }
}

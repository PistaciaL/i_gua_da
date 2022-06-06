package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.User;

public interface UserService {

    /**
     * 用户修改信息(用户只能修改名字，学号，邮箱三种信息，且不能重复)
     * @param user 修改信息后的用户
     * @return 1：修改成功；0：用户名字重复或学号重复或邮箱错误
     */
    public Integer setUserInformation(User user);

    /**
     * 用户修改密码
     * @param user 用户的原密码
     * @return true 修改成功
     */
    public boolean setUserpassword(User user);

    /**
     * 根据用户名，返回用户权限
     * @param userId 用户名字
     * @return 用户权限等级
     */
    public Integer getUserpermission(Integer userId);

    /**
     * 根据用户名查找用户
     * 这个方法重复了，但是我controller里面很多接口用了这个，把之前的实现方式粘过来就行
     * (所有查询失败的结果都返回null)
     * @param UserName 用户名
     * @return User用户
     */
    public User getUser(String UserName);

    /**
     * 通过用户邮箱查找用户
     * (所有查询失败的结果都返回null)
     * @param email 用户邮箱
     * @return User用户，如果没有该邮箱返回null
     */
    public User getUser1(String email);

    /**
     * 查询用户状态
     * @param user 用户
     * @return 1：正常状态，2：封禁状态
     */
    public Integer getUserStatus(User user);
}

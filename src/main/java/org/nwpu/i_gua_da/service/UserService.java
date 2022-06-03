package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.User;

public interface UserService {

    /**
     * 用户修改信息
     * @param user 修改信息后的用户
     * @return 1：修改成功；2：用户名字重复；3：用户邮箱重复
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
     * @param UserName 用户名字
     * @return 用户权限等级
     */
    public Integer getUserpermission(String UserName);
}

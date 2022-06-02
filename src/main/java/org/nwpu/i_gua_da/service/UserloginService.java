package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.User;

public interface UserloginService {

    /**
     * 验证用户密码是否正确
     * @param user 待验证的user(需要传递name&password)
     * @return true: 验证成功<br/>false: 验证失败
     */
    public boolean verifyUser(User user);

    /**
     * 用户注册，数据库中添加用户
     * @param user 注册的用户(需要传递name&studentNumber&email&password)
     * @return true:添加用户成功
     */
    public boolean addUser(User user);
}

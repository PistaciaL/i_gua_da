package org.nwpu.i_gua_da.service;

public interface UserService {

    /**
     * 用户修改信息
     * @param username 修改后的用户名
     * @param studentNumber 修改后的学号
     * @param email 修改后的邮箱
     * @return 返回修改结果用","隔开，若用户名重复，返回"$error"
     */
    public String setUserInformation(String username, String studentNumber, String email);

    /**
     * 用户修改密码
     * @param password 用户的原密码
     * @param newpassword 用户修改后的新密码
     * @return 返回用户修改过的新密码,若原密码错误，返回"$error"
     */
    public String setUserpassword(String password, String newpassword);

    /**
     * 根据用户名，返回用户权限
     * @param UserName 用户名字
     * @return 用户权限等级
     */
    public Integer getUserpermission(String UserName);
}

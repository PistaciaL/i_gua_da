package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.User;

public interface AdminService {

    /**
     * 发布公告
     * @return true发布成功
     */
    public boolean addNotice(String NoticeTitle, String Noticecontent);

    /**
     * 设置用户为管理员，将用户权限等级改为2
     * @param UserId 用户的id
     * @return true 更改成功
     */
    public boolean setUserAdmin(Integer UserId);

    /**
     * 取消管理员权限，将用户权限等级设为1
     * @param UserId 用户id
     * @param adminName 管理员名字(用来判断，此次操作是不是管理自己取消自己，如果name对应的id与userid相等，则设置失败)
     * @return true 更改成功，false 更改失败(管理员不能取消自己为管理员)
     */
    public boolean setUser(Integer UserId, String adminName);

    /**
     * 根据用户id查找用户
     * @param UserId 用户id
     * @return 查找的用户(不存在返回空)
     */
    public User searchUser(Integer UserId);

    /**
     * 根据用户名字查找用户
     * @param UserName 用户名字
     * @return 查找的用户(不存在返回空)
     */
    public User searchUser(String UserName);

    /**
     * 软删除用户
     * @param userId 根据用户id删除用户
     * @return true:删除成功
     */
    public boolean removeUser(Integer userId);
}

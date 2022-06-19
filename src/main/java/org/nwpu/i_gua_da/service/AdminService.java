package org.nwpu.i_gua_da.service;

import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Notice;
import org.nwpu.i_gua_da.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdminService {

    /**
     * 设置用户为管理员，将用户权限等级改为2
     * @param UserId 用户的id
     * @return true 更改成功
     */
    public boolean setUserAdmin(Integer UserId);

    /**
     * 取消管理员权限，将用户权限等级设为1
     * @param UserId 用户id
     * @param AdminName 管理员名字(用来判断，此次操作是不是管理自己取消自己，如果name对应的id与userid相等，则设置失败)
     * @return true 更改成功，false 更改失败(管理员不能取消自己为管理员)
     */
    public boolean setUser(Integer UserId, String AdminName);

    /**
     * 管理员查看所有用户
     * (所有查询失败的结果都返回null)
     * @param PageNum 页码数
     * @param PageSize 一页大小
     * @return 返回用户列表
     */
    public PageInfo<User> getUserList(Integer PageNum, Integer PageSize);

    /**
     * 根据用户id查找用户
     * (所有查询失败的结果都返回null)
     * @param UserId 用户id
     * @return 查找的用户(不存在返回空)
     */
    public User searchUser(Integer UserId);

    /**
     * 根据用户名字查找用户
     * (所有查询失败的结果都返回null)
     * @param UserName 用户名字
     * @return 查找的用户(不存在返回空)
     */
    public User searchUser(String UserName);

    /**
     * 软删除用户
     * @param UserId 根据用户id删除用户
     * @return true:删除成功
     */
    public boolean removeUser(Integer UserId);

    /**
     * 恢复用户的封禁
     * @param UserId 用户id
     * @return true：恢复成功 / false恢复失败
     */
    public boolean recoverUser(Integer UserId);

    /**
     * 根据用户名进行模糊搜索
     * @param userName
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<User> listUserByLikeUserName(String userName, Integer pageNum, Integer pageSize);

    PageInfo<User> listUserByLikeStudentNumber(int studentNumber, int page, int pageSize);

    boolean setUserPermission(int userId, int permission);
}

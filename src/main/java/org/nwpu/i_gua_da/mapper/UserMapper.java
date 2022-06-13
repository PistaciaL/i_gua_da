package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

    int setUserAsAdmin(@Param("userId") Integer userId, @Param("adminCode") int adminCode, @Param("notDeleteStatus") int notDeleteStatus);

    int setUserAsCommon(@Param("userId") Integer userId, @Param("adminName") String adminName, @Param("commonCode") Integer commonCode, @Param("notDeleteStatus") int notDeleteStatus);

    User selectUserById(Integer userId);

    User selectUserByName(String userName);

    int setUserStatusByUserId(@Param("userId") Integer userId, @Param("status") Integer status);

    /**
     * 对用户名和密码进行验证
     * @param user
     * @return 不为null则验证成功
     */
    Integer selectForVerify(User user);

    int addUser(User user);

    int setUserPassword(User user);

    /**
     * 查重, 用户名|学号|邮箱有一个重复则返回不为空
     * @param user
     * @return
     */
    Integer verifyByNameOrStudentNumbOrEmail(User user);
    /**
     * 查重, 用户名|邮箱有一个重复则返回不为空
     * @param user
     * @return
     */
    Integer verifyByNameOrEmail(User user);

    /**
     *  设置新的用户名+学号+邮箱
     * @param user
     * @return
     */
    int setUserInformation(User user);

    Integer getUserPermissionByUserId(Integer userId);

    List<User> getAllUser();

    User getUserByEmail(String email);

    Integer getUserStatusByUserId(Integer userId);

    List<User> listUserByLikeUserName(String userName);
}

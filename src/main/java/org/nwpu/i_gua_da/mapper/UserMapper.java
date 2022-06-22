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

    int setUserStatusByUserId(@Param("userId") Integer userId,
                              @Param("status") Integer status);

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

    List<User> getAllUser(@Param("userId") Integer userId);

    User getUserByEmail(@Param("email") String email);

    Integer getUserStatusByUserId(Integer userId);

    List<User> listUserByLikeUserName(String userName);
    
    /**
     * 用户第一次注册登录，赋给openid和本次登录的code
     * @param openid
     * @param code
     * @return
     */
    int setUserOpenidAndCode(@Param("userId") Integer userId, @Param("openid") String openid, @Param("code") String code);
    
    /**
     * 用户再一次登录，更新code
     * @param code
     * @return
     */
    int updateCode(@Param("userId") Integer userId, @Param("code") String code);
    
    /**
     * 通过code获取对应的user
     * @param code
     * @return
     */
    User getUserByCode(@Param("code") String code);

    User getUserByOpenid(@Param("openid") String openid);

    List<User> listUserByLikeStudentNumber(@Param("userId") int userId,@Param("studentNumber") int studentNumber);

    int setUserPermission(int userId, int permission);

    int updateUserByOpenid(@Param("openid") String openid, @Param("nickname") String nickname,
                           @Param("studentNumber") String studentNumber, @Param("email") String email);

    User getUserByStudentNumber(@Param("studentNumber") String studentNumber);

    int incrementCredit(@Param("userId") Integer userId);

    int decrementCredit(@Param("userId") Integer userId);
}

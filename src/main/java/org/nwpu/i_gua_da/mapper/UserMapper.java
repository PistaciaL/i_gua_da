package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.User;

@Mapper
public interface UserMapper {

    int setUserAsAdmin(@Param("userId") Integer userId, @Param("adminCode") int adminCode, @Param("notDeleteStatus") int notDeleteStatus);

    int setUserAsCommon(@Param("userId") Integer userId, @Param("adminName") String adminName, @Param("commonCode") Integer commonCode, @Param("notDeleteStatus") int notDeleteStatus);

    User selectUserById(Integer userId);

    User selectUserByName(String userName);

    int deleteUserById(@Param("userId") Integer userId, @Param("isDeleteStatus") Integer isDeleteStatus);
}

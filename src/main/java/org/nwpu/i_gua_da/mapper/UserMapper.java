package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nwpu.i_gua_da.entity.User;

@Mapper
public interface UserMapper {

    User selectUserTest();
}

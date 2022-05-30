package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.nwpu.i_gua_da.entity.TestEntity;
import org.nwpu.i_gua_da.entity.User;

@Mapper
public interface TestMapper {
    @Select("select * from test_entity limit 1")
    TestEntity testSelect();
}

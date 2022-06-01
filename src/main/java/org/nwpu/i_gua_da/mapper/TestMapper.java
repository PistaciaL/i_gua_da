package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.nwpu.i_gua_da.entity.TestEntity;
import org.nwpu.i_gua_da.entity.User;

/**
 * 测试Mapper类, 没有具体作用<br/>
 * 项目正式上线后删除
 */
@Mapper
public interface TestMapper {

    TestEntity testSelect();

    Integer testInsert(TestEntity testEntity);
}

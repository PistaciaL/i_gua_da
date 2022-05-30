package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.TestEntity;
import org.springframework.stereotype.Service;

public interface TestService {

    /**
     * 测试select方法
     * @return 数据库内select的测试实体类
     */
    public TestEntity testField();

    /**
     * insert方法测试
     * @param testEntity 需要insert的实体类(主键自增)
     * @return 变动的行数
     */
    public Integer insertTestField(TestEntity testEntity);
}

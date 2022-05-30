package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.TestEntity;
import org.springframework.stereotype.Service;

public interface TestService {

    /**
     * 测试select方法
     * @return 数据库内select的测试实体类
     */
    public TestEntity testField();
}

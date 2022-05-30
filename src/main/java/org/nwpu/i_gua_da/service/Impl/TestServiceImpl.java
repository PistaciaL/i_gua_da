package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.TestEntity;
import org.nwpu.i_gua_da.mapper.TestMapper;
import org.nwpu.i_gua_da.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public TestEntity testField() {
        return testMapper.testSelect();
    }
}

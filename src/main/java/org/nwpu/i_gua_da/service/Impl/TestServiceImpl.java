package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public String testField(String test1, String test2) {
        return "test1: "+ test1 + "\ntest2: " + test2;
    }
}

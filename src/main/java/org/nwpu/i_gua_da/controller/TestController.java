package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.api.CommonResult;
import org.nwpu.i_gua_da.api.ResultCode;
import org.nwpu.i_gua_da.service.Impl.TestServiceImpl;
import org.nwpu.i_gua_da.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestServiceImpl testService;

    @RequestMapping("/helloTest")
    public CommonResult helloTest() {
        int returnType = 2;
        if (returnType == 0)
            //成功, 只返回数据
            return CommonResult.success("dataInfo");
        else if(returnType == 1)
            //成功, 返回数据+message(后端的回复消息)
            return CommonResult.success("dataInfo", "messageInfoTest");
        else
            //失败, 返回IErrorCode的实现枚举类ResultCode(或自己实现IErrorCode返回)
            return CommonResult.failed(ResultCode.FAILED);
    }
}
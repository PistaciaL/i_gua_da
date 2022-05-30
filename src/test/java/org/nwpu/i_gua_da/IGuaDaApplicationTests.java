package org.nwpu.i_gua_da;

import org.junit.jupiter.api.Test;
import org.nwpu.i_gua_da.mapper.TestMapper;
import org.nwpu.i_gua_da.service.Impl.TestServiceImpl;
import org.nwpu.i_gua_da.service.TestService;
import org.nwpu.i_gua_da.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IGuaDaApplicationTests {

    @Autowired
    private TestServiceImpl testService;
//    @Autowired
//    private EmailSender emailSender;

    @Test
    void contextLoads() {
//        System.out.println(testService.testField("abc", "123"));
//        emailSender.send("523656497@qq.com", "验证码", "123456");
    }

}

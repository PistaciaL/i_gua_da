package org.nwpu.i_gua_da;

import org.junit.jupiter.api.Test;
import org.nwpu.i_gua_da.mapper.TestMapper;
import org.nwpu.i_gua_da.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IGuaDaApplicationTests {

//    @Autowired
//    private TestMapper testMapper;
    @Autowired
    private EmailSender emailSender;

    @Test
    void contextLoads() {
//        System.out.println(testMapper.testSelect());
        emailSender.send("523656497@qq.com", "验证码", "123456");
    }

}

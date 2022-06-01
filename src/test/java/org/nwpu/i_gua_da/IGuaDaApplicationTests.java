package org.nwpu.i_gua_da;

import org.junit.jupiter.api.Test;
import org.nwpu.i_gua_da.entity.TestEntity;
import org.nwpu.i_gua_da.mapper.TestMapper;
import org.nwpu.i_gua_da.service.Impl.TestServiceImpl;
import org.nwpu.i_gua_da.service.TestService;
import org.nwpu.i_gua_da.util.EmailSender;
import org.nwpu.i_gua_da.util.Rsa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

@SpringBootTest
class IGuaDaApplicationTests {

    @Autowired
    private TestServiceImpl testService;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private Rsa rsa;

    @Test
    void contextLoads() {

    }

    @Test
    void DatabaseOperationTest() {
        System.out.println(testService.testField());
        System.out.println(testService.insertTestField(new TestEntity("insertContentTest1", "insertContentTest2", LocalDateTime.now())));
        System.out.println(testService.testField());
    }

    @Test
    void emailSenderTest() {
        emailSender.send("523656497@qq.com", "验证码", "123456");
    }

    @Test
    void RSATest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String str = "123456你好";
        String result;
        String sercet = Rsa.publicEncrypt(str,Rsa.getPublicKey(Rsa.publicKey));
        result = Rsa.privateDecrypt(sercet,Rsa.getPrivateKey(Rsa.privateKey));
        System.out.println(sercet);
        System.out.println(result);
        System.out.println(result.equals(str));
    }
}

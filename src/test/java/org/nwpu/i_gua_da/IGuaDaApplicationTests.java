package org.nwpu.i_gua_da;

import org.junit.jupiter.api.Test;
import org.nwpu.i_gua_da.entity.TestEntity;
import org.nwpu.i_gua_da.service.Impl.TestServiceImpl;
import org.nwpu.i_gua_da.util.EmailSender;
import org.nwpu.i_gua_da.util.Rsa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
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
    void emailSenderTest() throws MessagingException {
        emailSender.send("523656497@qq.com", "123456", "注册账号");
    }

    @Test
    void RSATest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String str = "123456你好";
        String result;
        String secret = Rsa.publicEncrypt(str,Rsa.getPublicKey(Rsa.publicKey));
        result = Rsa.privateDecrypt(secret,Rsa.getPrivateKey(Rsa.privateKey));
        System.out.println(secret);
        System.out.println(result);
        System.out.println(result.equals(str));
    }
}

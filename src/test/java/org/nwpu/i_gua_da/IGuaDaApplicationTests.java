package org.nwpu.i_gua_da;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.junit.jupiter.api.Test;
import org.nwpu.i_gua_da.entity.*;
import org.nwpu.i_gua_da.mapper.UserMapper;
import org.nwpu.i_gua_da.service.*;
import org.nwpu.i_gua_da.service.Impl.ScheduleServiceImpl;
import org.nwpu.i_gua_da.service.Impl.TestServiceImpl;
import org.nwpu.i_gua_da.util.EmailSender;
import org.nwpu.i_gua_da.util.Rsa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class IGuaDaApplicationTests {

    @Autowired
    private TestServiceImpl testService;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private Rsa rsa;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserloginService userloginService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ScheduleServiceImpl scheduleServiceImpl;
    @Autowired
    private ReserveService reserveService;

    @Test
    void contextLoads() {
        System.out.println((Integer) redisTemplate.opsForHash().get("LastSeat", 1));
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

    @Test
    void RedisConnectTest() {
        String code = verificationCodeService.createVerificationCode(10086);
        System.out.println("create code: "+code);
        boolean b = verificationCodeService.verifyCode(10086, code);
        System.out.println(b);
    }

    @Test
    @Transactional
    void AdminServiceTest() {

    }

    @Test
    @Transactional
    void NoticeServiceTest() {
        System.out.println(noticeService.searchNotice(1));
        Notice n1 = new Notice();
        n1.setSender(adminService.searchUser(1));
        n1.setCreateTime(LocalDateTime.now());
        n1.setTitle("testNoticeTitle");
        n1.setContent("testestestestets");
        noticeService.addNotice(n1);
        noticeService.addNotice(n1);
        System.out.println(noticeService.searchNotice(1));
        System.out.println(noticeService.getNoticeList(0,1));
        System.out.println(noticeService.searchNotice("testNoticeTitle", 0, 2));
        List<Notice> l = noticeService.getNoticeList(0, 10);
        for(Notice n : l) {
            System.out.println(n);
        }
    }

    @Test
    @Transactional
    void UserLoginServiceTest() {
//        User verifyUser = new User();
//        verifyUser.setName("testUser");
//        verifyUser.setPassword("testUser");
//        System.out.println(userloginService.verifyUser(verifyUser));
//        verifyUser.setName("1");
//        System.out.println(userloginService.verifyUser(verifyUser));
//        verifyUser.setName("testUser");
//        verifyUser.setPassword("1");
//        System.out.println(userloginService.verifyUser(verifyUser));
        User user = new User();
        user.setName("test");
        user.setPassword("testp");
        user.setEmail("123123@163.com");
        System.out.println(adminService.searchUser("test"));
        System.out.println(userloginService.addUser(user));
        System.out.println(adminService.searchUser("test"));
        System.out.println(userloginService.addUser(user));
        System.out.println(userloginService.verifyUser(user));
    }

    @Test
    @Transactional
    void UserServiceTest() {
        User user = new User();
        user.setName("test");
        user.setPassword("testp");
        user.setEmail("123123@163.com");
        System.out.println(adminService.searchUser("test"));
        System.out.println(userloginService.addUser(user));
        User gettedUser = adminService.searchUser("test");
        System.out.println(gettedUser);
        User newUser = new User();
        newUser.setUserId(gettedUser.getUserId());
        newUser.setName("testChanged");
        newUser.setStudentNumber(123123);
        newUser.setEmail("testEmail@163.com");
        System.out.println(userService.setUserInformation(newUser));
        System.out.println(adminService.searchUser(gettedUser.getUserId()));
        System.out.println("\n\n\n");
        User nUser = new User();
        nUser.setUserId(gettedUser.getUserId());
        nUser.setName("testChanged");
        nUser.setPassword("newnewPasswd");
        System.out.println(userService.setUserpassword(nUser));
        System.out.println(userloginService.verifyUser(nUser));
    }

    @Test
    void RedisScheduleTest() {
        Station s1 = new Station();
        s1.setStationId(1);
        s1.setStationName("testStation1");
        Station s2 = new Station();
        s2.setStationId(2);
        s2.setStationName("testStation2");
        redisTemplate.opsForHash().put("Station", 1, s1);
        redisTemplate.opsForHash().put("Station", 2, s2);
        Schedule schedule = new Schedule();
        schedule.setStartStation(s1);
        schedule.setEndStation(s2);
        schedule.setTotalSeat(50);
        scheduleServiceImpl.addSchedule(schedule);
    }

    @Test
    void ReserveRedisTest() {
        Reserve reserve = new Reserve();
        User user = new User();
        user.setUserId(1);
        reserve.setUser(user);
        reserve.setReserveTime(LocalDateTime.now());
        Station s1 = new Station();
        s1.setStationId(1);
        s1.setStationName("testStation1");
        Station s2 = new Station();
        s2.setStationId(2);
        s2.setStationName("testStation2");
        Schedule schedule = new Schedule();
        schedule.setScheduleId(2);
        schedule.setStartStation(s1);
        schedule.setEndStation(s2);
        schedule.setTotalSeat(50);
        reserve.setSchedule(schedule);
        reserveService.bookingReserve(reserve);
    }
}

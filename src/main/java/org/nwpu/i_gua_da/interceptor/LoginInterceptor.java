package org.nwpu.i_gua_da.interceptor;

import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.mapper.UserMapper;
import org.nwpu.i_gua_da.util.GetOpenId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 权限拦截器类
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private GetOpenId getOpenId;

    @Value("${constants.user.permission.default}")
    private int permission = 1;

    @Value("${constants.user.permission.admin}")
    private int admin = 2;

    @Value("${constants.user.status.default}")
    private int status = 1;

    /**
     * 判断用户是否登录
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String code = request.getParameter("code");
        if (code==null){
            throw new RuntimeException(" 未携带code值");
        }
        Object isLogin = redisTemplate.opsForValue().get("userLogin:" + code);
        System.out.println("isLogin==>"+isLogin);
        if (isLogin == null){
            //有可能是redis键值过期失效，为了确保正确需要查一次数据库
            User user = userMapper.getUserByCode(code);
            if (user!=null){
                //更新redis中code时效
                System.out.println("更新redis中code时效");
                redisTemplate.opsForValue().set("userLogin:" + code,1,5,TimeUnit.MINUTES);
                redisTemplate.opsForValue().set("userPermission:"+code,user.getPermission(),5,TimeUnit.MINUTES);
            }else {
                Map<String, Object> map = getOpenId.wxCheck(code);
                String openid = (String) map.get("openid");
                //根据code查用户是否存在
                user = userMapper.getUserByOpenid(openid);
                //用户第一次登录,数据库不存在该用户记录
                if (user==null){
                    user = new User();
                    user.setCode(code);
                    user.setOpenid(openid);
                    user.setRegisterDatetime(LocalDateTime.now());
                    user.setPermission(permission);
                    user.setStatus(status);
                    user.setName(openid+":user");
                    user.setPassword(openid+":password");
                    user.setEmail(openid+":email");
                    //存入redis
                    System.out.println("用户第一次登录,数据库不存在该用户记录");
                    redisTemplate.opsForValue().set("userLogin:"+code,1,5, TimeUnit.MINUTES);
                    redisTemplate.opsForValue().set("userPermission:"+code,user.getPermission(),5,TimeUnit.MINUTES);
                    //存入数据库
                    userMapper.addUser(user);
                    throw new RuntimeException("用户需要完善个人信息");
                }else {
                    //用户是新的登录,更新code
                    userMapper.updateCode(user.getUserId(),code);
                    //存入redis
                    System.out.println("用户是新的登录,更新code");
                    redisTemplate.opsForValue().set("userLogin:"+code,true,5, TimeUnit.MINUTES);
                    redisTemplate.opsForValue().set("userPermission:"+code,user.getPermission(),5,TimeUnit.MINUTES);
                    //用户还没有完善个人信息，拦截下来
                    if (user.getName().equals(openid+":user")){
                        throw new RuntimeException("用户需要完善个人信息");
                    }
                }
            }
        }
        Object permission = redisTemplate.opsForValue().get("userPermission:"+code);
        if (permission==null){
            throw new RuntimeException("未知异常");
        }
        int userPermission = (int) permission;
        if (request.getRequestURI().contains("manager") && userPermission!=admin){
            throw new RuntimeException("用户权限不足");
        }
        return true;
    }
}

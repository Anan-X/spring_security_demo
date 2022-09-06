package com.zn.security.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zn.security.domain.LoginUser;
import com.zn.security.utils.JWTEasyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    static StringRedisTemplate  stringRedisTemplate;

    private static final String REDIS_LONGIN_PRE = "login:";
    private static final String REDIS_LONGIN_TOKEN = "login:token:";


    // 解决 @Component 下 @Autowired 注入为null的情况
    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        MyAuthenticationSuccessHandler.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        logger.info("登录成功");

        LoginUser principal = (LoginUser) authentication.getPrincipal();

        // 认证通过了，使用userid生成一个jwt jwt 返回给前端
        String token = new JWTEasyUtil().createToken(principal.getUserEntity().getId());


        // 把token存入redis 并设过期时间
        this.stringRedisTemplate.opsForValue().set(REDIS_LONGIN_TOKEN+principal.getUserEntity().getId(),token,1, TimeUnit.HOURS);

        // 把用户的完整信息存入redis
        this.stringRedisTemplate.opsForValue().set(REDIS_LONGIN_PRE+principal.getUserEntity().getId(), JSONObject.toJSONString(principal));

        // 返回token给前端     前端拿到token 每次请求都要在head里携带token
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(token));
    }
}

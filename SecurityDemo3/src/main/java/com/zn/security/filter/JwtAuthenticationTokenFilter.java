package com.zn.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.log.Log;
import com.zn.security.domain.LoginUser;
import com.zn.security.utils.JWTEasyUtil;
import com.zn.security.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    StringRedisTemplate redisTemplate;

    private static final String REDIS_LONGIN_PRE = "login:";
    private static final String REDIS_LONGIN_TOKEN = "login:token:";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {



        // 获取token
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            filterChain.doFilter(request, response);
            return;
        }
        String userId = null;
        // 解析token
        try {
            Claims parseToken = new JWTEasyUtil().parseToken(token);
            userId = parseToken.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 首先判断token值过期没
        String redisToken = redisTemplate.opsForValue().get(REDIS_LONGIN_TOKEN + userId);
        if(StringUtils.isEmpty(redisToken) || (!redisToken.equals(token))){
            throw new AccountExpiredException("token过期，请重新登录");
        }

        // TODO 从redis中获取用户信息
        String userJson = redisTemplate.opsForValue().get(REDIS_LONGIN_PRE + userId);
        // 判断用户是否已经注销了
        if(Objects.isNull(userJson)){
            throw new AccountExpiredException("请重新登录");
//            throw new RuntimeException("请重新登录");
        }
        LoginUser loginUser = JSONObject.parseObject(userJson, LoginUser.class);
        // 存入SecurityContextHolder
        // TODO  获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}

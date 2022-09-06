package com.zn.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zn.security.domain.LoginUser;
import com.zn.security.domain.SmsCodeAuthenticationToken;
import com.zn.security.entity.SmsCodeEntity;
import com.zn.security.entity.UserEntity;
import com.zn.security.mapper.UserMapper;
import com.zn.security.service.LoginService;
import com.zn.security.utils.BaseResult;
import com.zn.security.utils.JWTEasyUtil;
import com.zn.security.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String REDIS_LONGIN_PRE = "login:";
    private static final String REDIS_LONGIN_TOKEN = "login:token:";



    @Override
    public BaseResult logout() {
        // TODO 获取SecurityContextHolder中的用户id
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        Long userId = principal.getUserEntity().getId();

        // 删除redis中的用户信息
        redisTemplate.delete(REDIS_LONGIN_PRE+userId);
        // 删除redis token值
        redisTemplate.delete(REDIS_LONGIN_TOKEN+userId);

        return BaseResult.ok("删除成功");
    }

    @Override
    public BaseResult sendCode(String phone) {

        //随机生成4位数字验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));


        //把验证码存储到redis中过期时间为240秒
        redisTemplate.opsForValue().set(REDIS_LONGIN_PRE+phone, code, 240, TimeUnit.SECONDS);
        return BaseResult.ok(code);
    }

}

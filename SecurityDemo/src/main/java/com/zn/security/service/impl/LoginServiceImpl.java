package com.zn.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zn.security.domain.LoginUser;
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

@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String REDIS_LONGIN_PRE = "login:";

    @Override
    public BaseResult login(UserEntity user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 验证认证成功
        if(Objects.isNull(authenticate)){
            throw  new RuntimeException("登录失败");
        }
        // 认证通过了，使用userid生成一个jwt jwt 返回给前端
        LoginUser principal = (LoginUser) authenticate.getPrincipal();
        String token = new JWTEasyUtil().createToken(principal.getUserEntity().getId());
        HashMap<String, String> map = new HashMap<>();
        map.put("token",token);
        BaseResult<HashMap> result = new BaseResult<>(200,"登录成功",map);

        //把完整的用户信息存入redis ，userid作为key
        redisTemplate.opsForValue().set(REDIS_LONGIN_PRE+principal.getUserEntity().getId(),JSONObject.toJSONString(principal));
        return result;
    }

    @Override
    public BaseResult logout() {
        // TODO 获取SecurityContextHolder中的用户id
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        Long userId = principal.getUserEntity().getId();

        // TODO 删除redis中的值
        redisTemplate.delete(REDIS_LONGIN_PRE+userId);
//        HashMap<String, String> map = new HashMap<>();

        return BaseResult.ok("删除成功");
    }
}

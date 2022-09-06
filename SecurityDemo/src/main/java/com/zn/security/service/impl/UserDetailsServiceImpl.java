package com.zn.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zn.security.domain.LoginUser;
import com.zn.security.entity.UserEntity;
import com.zn.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 查询用户信息
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        System.out.println("用户名："+username);
        wrapper.eq("username",username);

        UserEntity userEntity = userMapper.selectOne(wrapper);



        if(Objects.isNull(userEntity)){
            throw  new RuntimeException("用户不存在");
        }

        //TODO 查询对应权限信息

        LoginUser user = new LoginUser(userEntity,Arrays.asList("test","admin"));
        return user;
    }
}

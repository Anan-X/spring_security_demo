package com.zn.security.controller;

import com.zn.security.mapper.UserMapper;
import com.zn.security.utils.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('admin')")
    // 使用自定义校验
    @PreAuthorize("@mySecurityExpression.hasAuthority('system:user:list')")
    public String hello (){
        return "hello";
    }

    @GetMapping("/userinfo")
    public BaseResult userinfo(Authentication authentication){
        return BaseResult.ok(authentication.getPrincipal());
    }

    @GetMapping("/test")
    public BaseResult helloTest() {
        return BaseResult.ok("hello");
    }
}

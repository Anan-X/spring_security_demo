package com.zn.security.controller;

import com.zn.security.domain.SmsCodeAuthenticationToken;
import com.zn.security.mapper.UserMapper;
import com.zn.security.utils.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

//    @Autowired
//    SecurityContextHolder securityContextHolder;

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('admin')")
    // 使用自定义校验
    @PreAuthorize("@mySecurityExpression.hasAuthority('system:user:list')")
    public String hello (){
        return "hello";
    }


    @GetMapping("/test")
    public BaseResult test(Principal principal) {
//        SmsCodeAuthenticationToken authentication = (SmsCodeAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        return BaseResult.ok(principal.getName());
    }
}

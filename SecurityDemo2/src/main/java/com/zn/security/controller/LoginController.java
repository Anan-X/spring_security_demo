package com.zn.security.controller;

import com.zn.security.domain.SmsCodeAuthenticationToken;
import com.zn.security.entity.SmsCodeEntity;
import com.zn.security.entity.UserEntity;
import com.zn.security.service.LoginService;
import com.zn.security.utils.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public BaseResult login(@RequestBody UserEntity user){

        return loginService.login(user);
    }

    @PostMapping("/sms/login")
    public BaseResult smsLogin(@RequestBody SmsCodeEntity smsCodeEntity) {

        return BaseResult.ok("ok");
//        return  loginService.smsLogin(smsCodeEntity);
    }

    @PostMapping("/sms/login/test")
    public BaseResult smsLoginTest(@RequestBody SmsCodeEntity smsCodeEntity) {

        SmsCodeAuthenticationToken authentication = (SmsCodeAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        return BaseResult.ok(authentication);
//        return  loginService.smsLogin(smsCodeEntity);
    }

    @GetMapping("/loginout")
    public BaseResult loginOut() {
        return loginService.logout();
    }
}

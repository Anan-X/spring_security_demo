package com.zn.security.controller;

import com.zn.security.domain.SmsCodeAuthenticationToken;
import com.zn.security.entity.SmsCodeEntity;
import com.zn.security.entity.UserEntity;
import com.zn.security.service.LoginService;
import com.zn.security.utils.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/sms/login/test")
    public BaseResult smsLoginTest(@RequestBody SmsCodeEntity smsCodeEntity) {

        SmsCodeAuthenticationToken authentication = (SmsCodeAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        return BaseResult.ok(authentication);
//        return  loginService.smsLogin(smsCodeEntity);
    }

    @GetMapping("/sms/sendcode")
    public BaseResult sendCode(@RequestParam("phone") String phone) {
        return loginService.sendCode(phone);
    }

    @GetMapping("/loginout")
    public BaseResult loginOut() {
        return loginService.logout();
    }
}

package com.zn.security.controller;

import com.zn.security.entity.UserEntity;
import com.zn.security.service.LoginService;
import com.zn.security.utils.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/loginout")
    public BaseResult loginOut() {
        return loginService.logout();
    }
}

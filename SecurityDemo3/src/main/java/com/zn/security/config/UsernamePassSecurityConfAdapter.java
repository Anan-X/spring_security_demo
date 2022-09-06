package com.zn.security.config;

import com.zn.security.filter.MyUserPassAuthenticationFilter;
import com.zn.security.handler.MyAuthenticationSuccessHandler;
import com.zn.security.provider.UsernamePassAuthenticationProvider;
import com.zn.security.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class UsernamePassSecurityConfAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {


        //自定义MyUserPassAuthenticationFilter过滤器
        MyUserPassAuthenticationFilter myUserPassAuthenticationFilter = new MyUserPassAuthenticationFilter();
        myUserPassAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        myUserPassAuthenticationFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());

        //设置自定义UsernamePassAuthenticationProvider的认证器userDetailsService
        UsernamePassAuthenticationProvider usernamePassAuthenticationProvider = new UsernamePassAuthenticationProvider(userDetailsService);

        //在UsernamePasswordAuthenticationFilter过滤前执行
        http.authenticationProvider(usernamePassAuthenticationProvider)
                .addFilterAfter(myUserPassAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}

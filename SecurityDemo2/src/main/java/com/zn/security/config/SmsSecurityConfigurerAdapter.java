package com.zn.security.config;

import com.zn.security.filter.SmsAuthenticationFilter;
import com.zn.security.handler.SmsAuthenticationSuccessHandler;
import com.zn.security.provider.SmsAuthenticationProvider;
import com.zn.security.service.impl.SmsDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class SmsSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private SmsDetailsServiceImpl userDetailsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void configure(HttpSecurity http) throws Exception {




        //自定义SmsCodeAuthenticationFilter过滤器
        SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
        smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsAuthenticationFilter.setAuthenticationSuccessHandler(new SmsAuthenticationSuccessHandler());

        //设置自定义SmsCodeAuthenticationProvider的认证器userDetailsService
        SmsAuthenticationProvider smsCodeAuthenticationProvider = new SmsAuthenticationProvider(userDetailsService);

        //在UsernamePasswordAuthenticationFilter过滤前执行
        http.authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}

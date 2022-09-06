package com.zn.security.config;

import com.zn.security.filter.JwtAuthenticationTokenFilter;
import com.zn.security.handler.MyAccessDeniedHandlerImpl;
import com.zn.security.handler.MyAuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    MyAuthenticationEntryPointImpl myAuthenticationEntryPoint;

    @Autowired
    MyAccessDeniedHandlerImpl myAccessDeniedHandler;

    // 创建盐值加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // 不通过session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 对于登录接口 运行匿名访问
                .authorizeRequests()
                .antMatchers("/login","/userinfo").anonymous()
                .antMatchers("/").hasAnyAuthority("admin")
                // 除上面外的所有请求全部要鉴权认证
                .anyRequest().authenticated();
        // 添加过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 配置异常处理器
//        http.exceptionHandling()
//                // 配置认证失败处理器
//                .authenticationEntryPoint(myAuthenticationEntryPoint)
//                // 配置权限处理器
//                .accessDeniedHandler(myAccessDeniedHandler);
        // 允许跨域
//        http.cors();
    }
}

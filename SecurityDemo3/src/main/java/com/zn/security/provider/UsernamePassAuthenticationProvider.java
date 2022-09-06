package com.zn.security.provider;

import com.zn.security.domain.LoginUser;
import com.zn.security.service.impl.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UsernamePassAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsServiceImpl userDetailsService;

    public UsernamePassAuthenticationProvider() {
    }

    public UsernamePassAuthenticationProvider(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        // 获取凭证也就是用户的用户名
        Object principal = authentication.getPrincipal();
        // 获取密码
        Object credentials = authentication.getCredentials();
        String username = "";
        if (principal instanceof String) {
            username = (String) principal;
        }

        String password = "";
        if (credentials instanceof String) {
            password = (String) credentials;
        }

        String inputCode = (String) authentication.getCredentials(); // 获取输入的验证码

        LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(username);

        if (!new BCryptPasswordEncoder().matches(password, loginUser.getPassword())) {
            throw new BadCredentialsException("账号密码不正确，请重新输入");
        }

        // 3. 重新创建已认证对象,
        UsernamePasswordAuthenticationToken authenticationResult = new UsernamePasswordAuthenticationToken(loginUser,loginUser.getPassword(), loginUser.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    // 当类型为UsernamePasswordAuthenticationToken的认证实体进入时才走此Provider
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

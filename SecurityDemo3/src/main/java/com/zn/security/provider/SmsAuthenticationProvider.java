package com.zn.security.provider;

import com.zn.security.domain.LoginUser;
import com.zn.security.domain.SmsCodeAuthenticationToken;
import com.zn.security.service.impl.SmsDetailsServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private static final String REDIS_LONGIN_PRE = "login:";


    private SmsDetailsServiceImpl smsUserDetailsService;

    private StringRedisTemplate stringRedisTemplate;


    public SmsAuthenticationProvider (SmsDetailsServiceImpl userDetailsServiceImpl,StringRedisTemplate stringRedisTemplate) {
        this.smsUserDetailsService = userDetailsServiceImpl;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        Object principal = authentication.getPrincipal();// 获取凭证也就是用户的手机号
        String phone = "";
        if (principal instanceof String) {
            phone = (String) principal;
        }
        String inputCode = (String) authentication.getCredentials(); // 获取输入的验证码


//         1. 检验Redis手机号的验证码
        String redisCode =stringRedisTemplate.opsForValue().get(REDIS_LONGIN_PRE+phone);
        if (StringUtils.isEmpty(redisCode)) {
            throw new BadCredentialsException("验证码已经过期或尚未发送，请重新发送验证码");
        }
        if (!inputCode.equals(redisCode)) {
            throw new BadCredentialsException("输入的验证码不正确，请重新输入");
        }
        // 2. 短信验证成功后要删除redis中的验证码
        stringRedisTemplate.delete(REDIS_LONGIN_PRE+phone);

        // 3. 根据手机号查询用户信息
        LoginUser userDetails = (LoginUser) smsUserDetailsService.loadUserByUsername(phone);
        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("phone用户不存在，请注册");
        }
        // 4. 重新创建已认证对象,
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails,inputCode, userDetails.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }
    // 当类型为SmsCodeAuthenticationToken的认证实体进入时才走此Provider
    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

}

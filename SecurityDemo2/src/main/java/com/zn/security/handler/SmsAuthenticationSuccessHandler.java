package com.zn.security.handler;

import com.alibaba.fastjson.JSON;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
@Component
public class SmsAuthenticationSuccessHandler implements AuthenticationSuccessHandler {




    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("登录成功");

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(authentication));
    }
}

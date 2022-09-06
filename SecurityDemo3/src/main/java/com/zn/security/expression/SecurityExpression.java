package com.zn.security.expression;


import com.zn.security.domain.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("mySecurityExpression")
public class SecurityExpression {

    public boolean hasAuthority(String authority) {
        // 获取当前用户的权限

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        List<String> permissions = principal.getPermissions();
        // 判断用户权限集合是否存在authority
        return permissions.contains(authority);

    }

}

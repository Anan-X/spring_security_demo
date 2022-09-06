package com.zn.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * jwt工具类
 */
@Slf4j
@Data
@Component
//@ConfigurationProperties("jwt.data")
public class JWTEasyUtil {

    //密钥
    public String SECRET = "zhangnan";
    //Authorization
    private String header;

    //创建token
    //传入userid
    public String createToken(Long userId){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,24*60*60*7);
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(userId+"")
                .setIssuedAt(new Date())
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256,SECRET);
        return builder.compact();
    }

    //校验jwt
    public Claims parseToken(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.error("jwt match error:{}",e);
            return null;
        }
    }

    //判断token是否过期
    public boolean judgeTokenExpiration(Date expiration){
        return expiration.before(new Date());
    }
}



package com.zn.security;

import com.zn.security.entity.UserEntity;
import com.zn.security.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void selectTest (){
        List<UserEntity> users = userMapper.selectList(null);
        for (UserEntity user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void BCryptPasswordEncoderTest() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 进行加密  获得密文
//        String passEncoder = passwordEncoder.encode("admin");
//        System.out.println("密文:" + passEncoder);

        // 验证密文
        boolean matches = passwordEncoder.matches("admin", "$2a$10$u9OAekP2XkR9NcHvyQ./N.1siMqOEy7vK2e/bJIXZKPDmiOAgf5fO");
        System.out.println(matches);
    }

    @Test
    public void redisTest() {
        // 以 key - value 键值对存储信息
        redisTemplate.opsForValue().set("123","admin",5, TimeUnit.MINUTES);

        // 根据 key 获取 value
        String value = redisTemplate.opsForValue().get("123");
        System.out.println("123: " + value);
    }

}

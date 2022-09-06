package com.zn.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zn.security.entity.UserEntity;
import com.zn.security.utils.BaseResult;

public interface LoginService extends IService<UserEntity> {
    BaseResult login(UserEntity user);

    BaseResult logout();

}

package com.zn.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zn.security.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}

package com.zn.security.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.zn.security.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class LoginUser implements UserDetails {
    private UserEntity userEntity;

    public LoginUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    private List<String> permissions;

    @JSONField(serialize = false)
    List<SimpleGrantedAuthority> authorities;

    public LoginUser(UserEntity userEntity, List<String> permissions) {
        this.userEntity = userEntity;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities!=null)
            return authorities;
        authorities = permissions.stream().map(item -> {
            return new SimpleGrantedAuthority(item);
        }).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

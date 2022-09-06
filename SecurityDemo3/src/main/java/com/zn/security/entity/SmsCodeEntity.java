package com.zn.security.entity;

import lombok.Data;

@Data
public class SmsCodeEntity {

    private String phone;
    private String code;
}

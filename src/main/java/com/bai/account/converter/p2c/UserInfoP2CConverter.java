package com.bai.account.converter.p2c;


import com.bai.account.model.persistence.UserInfo;

import com.google.common.base.Converter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserInfoP2CConverter extends Converter<UserInfo, com.bai.account.model.common.UserInfo> {
    @Override
    protected com.bai.account.model.common.UserInfo doForward(UserInfo userInfo) {
        return com.bai.account.model.common.UserInfo.builder()
                                                    .id(userInfo.getId())
                                                    .username(userInfo.getUsername())
                                                    .password(userInfo.getPassword())
                                                    .salt(userInfo.getSalt())
                                                    .build();
    }

    @Override
    protected UserInfo doBackward(com.bai.account.model.common.UserInfo userInfo) {
        return  UserInfo.builder()
                        .id(userInfo.getId())
                        .username(userInfo.getUsername())
                        .password(userInfo.getPassword())
                        .build();
    }
}

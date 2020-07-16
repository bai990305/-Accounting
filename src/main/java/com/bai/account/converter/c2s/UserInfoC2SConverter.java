package com.bai.account.converter.c2s;

import com.bai.account.model.common.UserInfo;

import com.google.common.base.Converter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserInfoC2SConverter extends Converter<UserInfo, com.bai.account.model.service.UserInfo> {

    @Override
    protected com.bai.account.model.service.UserInfo doForward(UserInfo userInfo) {
        return com.bai.account.model.service.UserInfo.builder()
                                                     .id(userInfo.getId())
                                                     .username(userInfo.getUsername())
                                                     .build();
    }

    @Override
    protected UserInfo doBackward(com.bai.account.model.service.UserInfo userInfo) {
        return UserInfo.builder()
                        .id(userInfo.getId())
                        .username(userInfo.getUsername())
                        .build();
    }
}

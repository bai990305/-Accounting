package com.bai.account.service;

import com.bai.account.model.common.UserInfo;

public interface UserInfoService {

    UserInfo findUserInfoById(Long id);

    UserInfo findUserInfoByUserName(String username);

    void login(String username, String password);

    UserInfo register(String username, String password);

}

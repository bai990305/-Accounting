package com.bai.account.dao;

import com.bai.account.model.persistence.UserInfo;


@SuppressWarnings("checkstyle:RegexpSingleline")
public interface UserInfoDao {

    UserInfo findUserInfoById(Long id);

    UserInfo findUserInfoByUserName(String username);

    void createNewUser(UserInfo userInfo);
}

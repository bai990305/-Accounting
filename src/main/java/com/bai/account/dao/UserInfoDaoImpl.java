package com.bai.account.dao;

import com.bai.account.dao.mapper.UserInfoMapper;
import com.bai.account.model.persistence.UserInfo;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserInfoDaoImpl implements UserInfoDao {
    private UserInfoMapper mapper;
    @Autowired
    public UserInfoDaoImpl(UserInfoMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserInfo findUserInfoById(Long id) {
        return mapper.findUserInfoById(id);
    }

    @Override
    public UserInfo findUserInfoByUserName(String username) {
        return mapper.findUserInfoByUserName(username);
    }

    @Override
    public void createNewUser(UserInfo userInfo) {
        val row = mapper.createNewUser(userInfo);
        log.debug("Result: {}, use information: {}", row, userInfo);
    }
}

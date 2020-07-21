package com.bai.account.service.impl;

import com.bai.account.converter.p2c.UserInfoP2CConverter;
import com.bai.account.dao.UserInfoDao;
import com.bai.account.exception.InvalidParameterException;
import com.bai.account.exception.ResourceNotFoundException;
import com.bai.account.model.common.UserInfo;
import com.bai.account.service.UserInfoService;

import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    public static final int HASH_ITERATIONS = 1000;
    private UserInfoDao userInfoDao;
    private final UserInfoP2CConverter userInfoP2CConverter;
    @Autowired
    public UserInfoServiceImpl(UserInfoDao userInfoDao,UserInfoP2CConverter userInfoP2CConverter) {
        this.userInfoDao = userInfoDao;
        this.userInfoP2CConverter = userInfoP2CConverter;
    }

    @Override
    public UserInfo findUserInfoById(Long id) {
        return Optional.ofNullable(userInfoDao.findUserInfoById(id))
            .map(userInfoP2CConverter::convert)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("not find id is %d user",id)));
    }

    @Override
    public UserInfo findUserInfoByUserName(String username) {
        return Optional.ofNullable(userInfoDao.findUserInfoByUserName(username))
            .map(userInfoP2CConverter::convert)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("not find username is %s user",username)));
    }

    @Override
    public void login(String username, String password) {
        val subject = SecurityUtils.getSubject();
        val usernamePasswordToken = new UsernamePasswordToken(username, password);
        //login
        subject.login(usernamePasswordToken);
    }

    @Override
    public UserInfo register(String username, String password) {
        val flag = userInfoDao.findUserInfoByUserName(username);
        if (flag != null) {
            throw new InvalidParameterException("The user name already exists");
        }
        //获取盐
        val salt = UUID.randomUUID().toString();
        //加密密码,次数1000次
        val encryptedPassword = new Sha256Hash(password,salt, HASH_ITERATIONS).toBase64();
        val newUserInfo = com.bai.account.model.persistence.UserInfo.builder()
            .username(username)
            .password(encryptedPassword)
            .salt(salt)
            .createTime(LocalDateTime.now())
            .build();
        userInfoDao.createNewUser(newUserInfo);
        return userInfoP2CConverter.convert(newUserInfo);
    }
}

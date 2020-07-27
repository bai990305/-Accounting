package com.bai.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.bai.account.converter.p2c.UserInfoP2CConverter;
import com.bai.account.dao.UserInfoDao;
import com.bai.account.exception.InvalidParameterException;
import com.bai.account.exception.ResourceNotFoundException;
import com.bai.account.model.persistence.UserInfo;
import com.bai.account.service.impl.UserInfoServiceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserInfoServiceTest {
    @Mock
    private UserInfoDao userInfoDao;
    private UserInfoService userInfoService;
    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        userInfoService = new UserInfoServiceImpl(userInfoDao, new UserInfoP2CConverter());
    }

    @Test
    void findUserInfoByIdTest(){
        //Arrange
        val id = 100L;
        val username = "bai";
        val password = "bai";
        val salt = "find By Id";
        val createTime = LocalDateTime.now();
        val userInfo = UserInfo.builder()
            .username(username)
            .id(id)
            .salt(salt)
            .password(password)
            .createTime(createTime)
            .build();
        doReturn(userInfo).when(userInfoDao).findUserInfoById(id);
        //Act
        val result = userInfoService.findUserInfoById(id);
        //Assert
        assertThat(result).isNotNull()
            .hasFieldOrPropertyWithValue("id",id)
            .hasFieldOrPropertyWithValue("username",username)
            .hasFieldOrPropertyWithValue("password",password)
            .hasFieldOrPropertyWithValue("salt",salt);
        verify(userInfoDao).findUserInfoById(eq(id));
        //错误测试
        doReturn(null).when(userInfoDao).findUserInfoById(id);
        assertThrows(ResourceNotFoundException.class,()->userInfoService.findUserInfoById(id));
    }
    @Test
    void findUserInfoByUserNameTest(){
        //Arrange
        val id = 100L;
        val username = "bai";
        val password = "bai";
        val salt = "find By Id";
        val createTime = LocalDateTime.now();
        val userInfo = UserInfo.builder()
            .username(username)
            .id(id)
            .salt(salt)
            .password(password)
            .createTime(createTime)
            .build();
        doReturn(userInfo).when(userInfoDao).findUserInfoByUserName(username);
        //Act
        val result = userInfoService.findUserInfoByUserName(username);
        //Assert
        assertThat(result).isNotNull()
            .hasFieldOrPropertyWithValue("id",id)
            .hasFieldOrPropertyWithValue("username",username)
            .hasFieldOrPropertyWithValue("password",password)
            .hasFieldOrPropertyWithValue("salt",salt);
        verify(userInfoDao).findUserInfoByUserName(eq(username));
        //错误测试
        doReturn(null).when(userInfoDao).findUserInfoByUserName(username);
        assertThrows(ResourceNotFoundException.class,()->userInfoService.findUserInfoByUserName(username));
    }
    @Test
    void testRegister() {
        //Arrange
        val id = 100L;
        val username = "bai2";
        val password = "123456";
        val salt = UUID.randomUUID().toString();
        //加密密码,次数1000次
        val encryptedPassword = new Sha256Hash(password,salt, 1000).toBase64();
        val userInfo = UserInfo.builder()
            .username(username)
            .id(id)
            .salt(salt)
            .password(encryptedPassword)
            .createTime(LocalDateTime.now())
            .build();
        doNothing().when(userInfoDao).createNewUser(any());
        doReturn(null).when(userInfoDao).findUserInfoByUserName(username);
        //Act
        val result = userInfoService.register(username,password);
        //Assert
        assertThat(result).isNotNull()
        .hasFieldOrPropertyWithValue("username","bai2");
        verify(userInfoDao).createNewUser(any());
        //错误测试
        doReturn(userInfo).when(userInfoDao).findUserInfoByUserName(username);
        assertThrows(InvalidParameterException.class,()->userInfoService.register(username,password));

    }

}

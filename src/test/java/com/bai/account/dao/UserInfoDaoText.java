package com.bai.account.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.bai.account.dao.mapper.UserInfoMapper;
import com.bai.account.model.persistence.UserInfo;

import java.time.LocalDateTime;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserInfoDaoText {

    @Mock
    private UserInfoMapper mapper;

    @InjectMocks
    private UserInfoDaoImpl dao;

    @Test
    void testGetUserInfoById(){
        //Arrange
        val id = 100L;
        val username = "bai";
        val password = "bai";
        val createTime = LocalDateTime.now();
        val userInfo = UserInfo.builder()
                                .id(id)
                                .username(username)
                                .password(password)
                                .createTime(createTime)
                                .build();
        doReturn(userInfo).when(mapper).findUserInfoById(id);
        //Act
        val result = dao.findUserInfoById(id);
        //Assert
        assertEquals(result,userInfo);
        verify(mapper).findUserInfoById(anyLong());
    }

    @Test
    void testGetUserInfoByUsername(){
        //Arrange
        val id = 100L;
        val username = "bai";
        val password = "bai";
        val createTime = LocalDateTime.now();
        val userInfo =  UserInfo.builder()
                                .id(id)
                                .username(username)
                                .password(password)
                                .createTime(createTime)
                                .build();
        doReturn(userInfo).when(mapper).findUserInfoByUserName(username);
        //Act
        val result = dao.findUserInfoByUserName(username);
        //Assert
        assertEquals(result,userInfo);
        verify(mapper).findUserInfoByUserName(username);
    }
}

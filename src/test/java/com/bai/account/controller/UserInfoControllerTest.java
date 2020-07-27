package com.bai.account.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bai.account.converter.c2s.UserInfoC2SConverter;
import com.bai.account.exception.GlobalExceptionHandler;
import com.bai.account.model.common.UserInfo;
import com.bai.account.service.UserInfoService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class UserInfoControllerTest {
    @Mock
    private UserInfoService userInfoService;
    private UserController userController;
    private MockMvc mockMvc;
    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userInfoService,new UserInfoC2SConverter());
        mockMvc= MockMvcBuilders.standaloneSetup(userController)
            .setControllerAdvice(new GlobalExceptionHandler()).build();
    }
    @Test
    void registerTest() throws Exception {
        //Arrange
        val id = 100L;
        val username = "bai";
        val password = "bai";
        val userInfo = UserInfo.builder()
                                .username(username)
                                .id(id)
                                .password(password)
                                .build();
        doReturn(userInfo).when(userInfoService).register(username,password);
        //Act&Assert
        mockMvc.perform(post("/v1.0/user")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(userInfo)
            )
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(content().string(new ObjectMapper().writeValueAsString(new UserInfoC2SConverter().convert(userInfo))));
        verify(userInfoService).register(username,password);
    }
    @Test
    void testFindUserById() throws Exception {
        //Arrange
        val id = 100L;
        val username = "bai";
        val password = "bai";
        val userInfo = UserInfo.builder()
            .username(username)
            .id(id)
            .password(password)
            .build();
        doReturn(userInfo).when(userInfoService).findUserInfoById(id);
        //Act
        mockMvc.perform(get("/v1.0/user/100")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string(new ObjectMapper().writeValueAsString(new UserInfoC2SConverter().convert(userInfo))));
        verify(userInfoService).findUserInfoById(any());
        //Assert
    }
}

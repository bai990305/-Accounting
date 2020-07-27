package com.bai.account.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;

import com.bai.account.exception.GlobalExceptionHandler;
import com.bai.account.model.service.UserInfo;
import com.bai.account.service.UserInfoService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {
    @Mock
    private UserInfoService userInfoService;

    @InjectMocks
    private SessionController sessionController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }
    @Test
    void loginTest() throws Exception {
        // Arrange
        val username = "hardcore";
        val password = "hardcore";

        val userInfo = UserInfo.builder()
            .username(username)
            .password(password)
            .build();

        doNothing().when(userInfoService).login(username, password);

        // Act && Assert
        mockMvc.perform(post("/v1.0/session")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(userInfo))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string("success"));

        verify(userInfoService).login(username, password);

    }
}

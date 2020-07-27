package com.bai.account.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bai.account.converter.c2s.TagC2SConverter;
import com.bai.account.converter.c2s.UserInfoC2SConverter;
import com.bai.account.exception.GlobalExceptionHandler;
import com.bai.account.model.common.Tag;
import com.bai.account.service.TagService;
import com.bai.account.service.UserInfoService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class TagControllerTest {
    @Mock
    private TagService service;
    @Mock
    private  UserInfoService userInfoService;
    private TagController tagController;
    private MockMvc mockMvc;
    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        tagController = new TagController(service,new TagC2SConverter(),userInfoService);
        mockMvc= MockMvcBuilders.standaloneSetup(tagController)
            .setControllerAdvice(new GlobalExceptionHandler()).build();
    }
    @Test
    void testGetTagByTagId() throws Exception {
        //Arrange
        val id = 100L;
        val description = "bai";
        val userId = 100L;
        val status = "ENABLE";
        val tag = Tag.builder()
            .description(description)
            .id(id)
            .userId(userId)
            .status(status)
            .build();
        doReturn(tag).when(service).findTagById(any());
        //Act&&Assert
        mockMvc.perform(get("/v1.0/tag/1")
            .contentType("application/json")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string(new ObjectMapper().writeValueAsString(new TagC2SConverter().convert(tag))));
        verify(service).findTagById(any());
        //错误测试
        mockMvc.perform(get("/v1.0/tag/-100")
            .contentType("application/json")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andExpect(content().string("{\"code\":\"INVALID_PARAMETER\",\"errorType\":\"Client\",\"message\":\"The tagId must be not empty and positive.\",\"statusCode\":400}"));
    }
    @Test
    void testCreateTag() throws Exception {
        val id = 100L;
        val description = "bai";
        val userId = 100L;
        val status = "ENABLE";
        val tag = Tag.builder()
            .description(description)
            .id(id)
            .userId(userId)
            .status(status)
            .build();
        val requestBody = com.bai.account.model.service.Tag.builder()
            .description(description)
            .userId(userId)
            .build();
        doReturn(tag).when(service).createTag(description,userId);
        //Act&&Assert
        mockMvc.perform(post("/v1.0/tag")
            .contentType("application/json")
            .accept(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(requestBody))
        ).andExpect(content().contentType("application/json"))
        .andExpect(content().string(new ObjectMapper().writeValueAsString(new TagC2SConverter().convert(tag))));
        verify(service).createTag(description,userId);
        //错误测试
        val requestBody1 = com.bai.account.model.service.Tag.builder()
            .userId(userId)
            .build();
        mockMvc.perform(post("/v1.0/tag")
            .contentType("application/json")
            .accept(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(requestBody1))
        ).andExpect(status().is4xxClientError())
            .andExpect(content().string("{\"code\":\"INVALID_PARAMETER\",\"errorType\":\"Client\",\"message\":\"The description and user id must be not null or empty.\",\"statusCode\":400}"));
    }
    @Test
    void testUpdateTag() throws Exception {
        // Arrange
        val tagId = 100L;
        val userId = 1900L;
        val description = "playing";

        val userInfo = com.bai.account.model.common.UserInfo.builder()
            .username("hardcore")
            .password("hardcore")
            .id(userId)
            .build();

        val requestBody = com.bai.account.model.service.Tag.builder()
            .id(tagId)
            .description(description)
            .userId(userId)
            .status("DISABLE")
            .build();

        val tagInCommon = Tag.builder()
            .id(tagId)
            .description(description)
            .userId(userId)
            .status("DISABLE")
            .build();

        doReturn(tagInCommon).when(service).updateTag(any(Tag.class));
        doReturn(userInfo).when(userInfoService).findUserInfoById(userId);

        // Act && Assert
        mockMvc.perform(put("/v1.0/tag/" + tagId)
            .content(new ObjectMapper().writeValueAsString(requestBody))
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string(new ObjectMapper().writeValueAsString(requestBody)));

        verify(service).updateTag(any(Tag.class));
        verify(userInfoService).findUserInfoById(anyLong());
        //错误测试
        //tagId<0
        mockMvc.perform(put("/v1.0/tag/-100" )
            .content(new ObjectMapper().writeValueAsString(requestBody))
            .contentType("application/json"))
            .andExpect(content().string("{\"code\":\"INVALID_PARAMETER\",\"errorType\":\"Client\",\"message\":\"The tagId must be not empty and positive.\",\"statusCode\":400}"));
        //getUserId<0
        val requestBody1 = com.bai.account.model.service.Tag.builder()
            .id(tagId)
            .description(description)
            .userId(-100L)
            .status("DISABLE")
            .build();
        mockMvc.perform(put("/v1.0/tag/"+tagId )
            .content(new ObjectMapper().writeValueAsString(requestBody1))
            .contentType("application/json"))
            .andExpect(content().string("{\"code\":\"INVALID_PARAMETER\",\"errorType\":\"Client\",\"message\":\"The userId must be not empty and positive.\",\"statusCode\":400}"));
        //findUserInfoById=null
        doReturn(null).when(userInfoService).findUserInfoById(userId);
        mockMvc.perform(put("/v1.0/tag/" + tagId)
            .content(new ObjectMapper().writeValueAsString(requestBody))
            .contentType("application/json"))
            .andExpect(content().string("{\"code\":\"RESOURCE_NOT_FOUND\",\"errorType\":\"Client\",\"message\":\"The userId not found1900\",\"statusCode\":404}"));
        //status=null
        doReturn(userInfo).when(userInfoService).findUserInfoById(userId);
        val requestBody2 = com.bai.account.model.service.Tag.builder()
            .id(tagId)
            .description(description)
            .userId(userId)
            .build();
        mockMvc.perform(put("/v1.0/tag/" + tagId)
            .content(new ObjectMapper().writeValueAsString(requestBody2))
            .contentType("application/json"))
            .andExpect(content().string("{\"id\":100,\"description\":\"playing\",\"userId\":1900,\"status\":\"DISABLE\"}"));
    }
}

package com.bai.account.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bai.account.converter.c2s.RecordC2SConverter;
import com.bai.account.exception.GlobalExceptionHandler;
import com.bai.account.model.common.Record;
import com.bai.account.model.common.Tag;
import com.bai.account.service.RecordService;
import com.bai.account.service.UserInfoService;

import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class RecordControllerTest {
    private MockMvc mockMvc;
    @Mock
    private RecordService recordService;
    @Mock
    private UserInfoService userService;
    @Spy
    private RecordC2SConverter c2SConverter;
    @InjectMocks
    private RecordController recordController;
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(recordController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }
    @Test
    void testFindRecordById() throws Exception {
        //Arrange
        val id = 100L;
        val userId = 200L;
        val amount = new BigDecimal(100.25);
        val tagId = 1L;
        val tagInCommon = Tag.builder()
            .id(tagId)
            .build();
        val recordInCommon = Record.builder()
            .amount(amount)
            .userId(userId)
            .note("xxxx")
            .category("INCOME")
            .tagList(ImmutableList.of(tagInCommon))
            .build();
        doReturn(recordInCommon).when(recordService).findRecordById(eq(id));
        //Act&Assert
        mockMvc.perform(get("/v1.0/record/" + id).contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
        .andExpect(content().string(new ObjectMapper().writeValueAsString(c2SConverter.convert(recordInCommon))));
        //错误检验
        mockMvc.perform(get("/v1.0/record/" + -1).contentType("application/json"))
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string("{\"code\":\"INVALID_PARAMETER\",\"errorType\":\"Client\",\"message\":\"The id is not allowed to be null or less than 0\",\"statusCode\":400}"));
        verify(recordService).findRecordById(any());
    }
    @Test
    void testCreateRecord() throws Exception {
        val id = 100L;
        val userId = 200L;
        val amount = new BigDecimal(100.25);
        val tagId = 1L;
        val tagInCommon = Tag.builder()
            .id(tagId)
            .build();
        val tagInService = com.bai.account.model.service.Tag.builder()
            .id(tagId)
            .build();
        val recordInCommon = Record.builder()
            .amount(amount)
            .userId(userId)
            .note("xxxx")
            .category("INCOME")
            .tagList(ImmutableList.of(tagInCommon))
            .build();
        val requestBody = com.bai.account.model.service.Record.builder()
            .amount(amount)
            .userId(userId)
            .note("xxxx")
            .category("INCOME")
            .tagList(ImmutableList.of(tagInService))
            .build();
        doReturn(recordInCommon).when(recordService).createRecord(recordInCommon);
        //Act&Assert
        mockMvc.perform(post("/v1.0/record/").contentType("application/json").content(new ObjectMapper().writeValueAsString(requestBody)))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string(new ObjectMapper().writeValueAsString(c2SConverter.convert(recordInCommon))));
        //错误检验
        val errorRequestBody = com.bai.account.model.service.Record.builder()
            .amount(amount)
            .userId(userId)
            .note("xxxx")
            .tagList(ImmutableList.of(tagInService))
            .build();
        mockMvc.perform(post("/v1.0/record/").contentType("application/json").content(new ObjectMapper().writeValueAsString(errorRequestBody)))
            .andExpect(status().is4xxClientError())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string("{\"code\":\"INVALID_PARAMETER\",\"errorType\":\"Client\",\"message\":\"invalid record to created\",\"statusCode\":400}"));
        verify(recordService).createRecord(any(Record.class));
    }
    @Test
    void testUpdateRecord() throws Exception {
        //Arrange
        val id = 100L;
        val userId = 200L;
        val amount = new BigDecimal(100.25);
        val tagId = 1L;
        val tagInCommon = Tag.builder()
            .id(tagId)
            .build();
        val tagInService = com.bai.account.model.service.Tag.builder()
            .id(tagId)
            .build();
        val recordInCommon = Record.builder()
            .amount(amount)
            .userId(userId)
            .note("xxxx")
            .category("INCOME")
            .tagList(ImmutableList.of(tagInCommon))
            .build();
        val requestBody = com.bai.account.model.service.Record.builder()
            .amount(amount)
            .userId(userId)
            .note("xxxx")
            .category("INCOME")
            .tagList(ImmutableList.of(tagInService))
            .build();
        doReturn(null).when(userService).findUserInfoById(any());
        doReturn(recordInCommon).when(recordService).updateRecord(any(Record.class));
        //Act&Assert
        mockMvc.perform(put("/v1.0/record/"+id).contentType("application/json").content(new ObjectMapper().writeValueAsString(requestBody)))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string(new ObjectMapper().writeValueAsString(c2SConverter.convert(recordInCommon))));
        //错误检查
        mockMvc.perform(put("/v1.0/record/"+ -1).contentType("application/json").content(new ObjectMapper().writeValueAsString(requestBody)))
            .andExpect(status().is4xxClientError())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string("{\"code\":\"INVALID_PARAMETER\",\"errorType\":\"Client\",\"message\":\"The record id must be not empty and positive.\",\"statusCode\":400}"));
        requestBody.setUserId(-100L);
        mockMvc.perform(put("/v1.0/record/"+ id).contentType("application/json").content(new ObjectMapper().writeValueAsString(requestBody)))
            .andExpect(status().is4xxClientError())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string("{\"code\":\"INVALID_PARAMETER\",\"errorType\":\"Client\",\"message\":\"The user id is empty or invalid\",\"statusCode\":400}"));
        verify(userService).findUserInfoById(any());
        verify(recordService).updateRecord(any(Record.class));
    }
}

package com.bai.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bai.account.converter.p2c.RecordP2CConverter;
import com.bai.account.dao.RecordDao;
import com.bai.account.dao.RecordTagDao;
import com.bai.account.dao.TagDao;
import com.bai.account.exception.InvalidParameterException;
import com.bai.account.exception.ResourceNotFoundException;
import com.bai.account.model.persistence.Record;
import com.bai.account.model.persistence.Tag;
import com.bai.account.service.impl.RecordServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecordServiceTest {
    @Mock
    private RecordDao recordDao;
    @Spy
    private RecordP2CConverter p2CConverter;
    @Mock
    private TagDao tagDao;
    @Mock
    private RecordTagDao recordTagDao;
    @InjectMocks
    private RecordServiceImpl recordService;
    @Test
    void testFindRecordById(){
        //Arrange
        val id = 100L;
        val userId = 200L;
        val amount = new BigDecimal(100.25);
        val tagId = 1L;
        val tagInCommon = Tag.builder()
            .id(tagId)
            .userId(userId)
            .description("playing")
            .status(1)
            .build();
        val recordInDB = Record.builder()
            .id(id)
            .userId(userId)
            .status(1)
            .tagList( ImmutableList.of(tagInCommon))
            .amount(amount)
            .note("Playing game")
            .category(0)
            .build();
        doReturn(recordInDB).when(recordDao).findRecordById(eq(id));
        //Act&Assert
        val result = recordService.findRecordById(id);
        assertThat(result).isNotNull().hasFieldOrPropertyWithValue("id", id)
            .hasFieldOrPropertyWithValue("userId", userId)
            .hasFieldOrPropertyWithValue("note", "Playing game")
            .hasFieldOrPropertyWithValue("category", "OUTCOME")
            .hasFieldOrPropertyWithValue("amount", amount);
        //错误测试
        doReturn(null).when(recordDao).findRecordById(eq(id));
        assertThrows(ResourceNotFoundException.class,()->recordService.findRecordById(id));
        verify(recordDao,times(2)).findRecordById(any());
    }
    @Test
    void testCreateRecord(){
        val id = 100L;
        val userId = 200L;
        val amount = new BigDecimal(100.25);
        val tagId = 1L;
        val tagInCommon = Tag.builder()
            .id(tagId)
            .userId(userId)
            .description("playing")
            .status(1)
            .build();
        val tagList = ImmutableList.of(tagInCommon);
        val recordInDB = Record.builder()
            .id(id)
            .userId(userId)
            .status(1)
            .tagList( ImmutableList.of(tagInCommon))
            .amount(amount)
            .note("Playing game")
            .category(0)
            .build();
        val recordIncommen = p2CConverter.convert(recordInDB);
        doNothing().when(recordDao).insertRecord(any());
        doNothing().when(recordTagDao).batchInsertRecordTag(tagList,id);
        doReturn(tagList).when(tagDao).findTagListById(anyList());
        doReturn(recordInDB).when(recordDao).findRecordById(id);
        //Act
        val result = recordService.createRecord(recordIncommen);
        //Assert
        assertThat(result).isNotNull()
            .hasFieldOrPropertyWithValue("id", id)
            .hasFieldOrPropertyWithValue("userId", userId)
            .hasFieldOrPropertyWithValue("note", "Playing game")
            .hasFieldOrPropertyWithValue("category", "OUTCOME")
            .hasFieldOrPropertyWithValue("amount", amount);
        //错误检测
        List<Tag> list = new ArrayList<>();
        doReturn(list).when(tagDao).findTagListById(anyList());
        assertThrows(InvalidParameterException.class,()->recordService.createRecord(recordIncommen));
        tagInCommon.setUserId(1L);
        val tagList2 = ImmutableList.of(tagInCommon);
        doReturn(tagList2).when(tagDao).findTagListById(anyList());
        assertThrows(InvalidParameterException.class,()->recordService.createRecord(recordIncommen));
        verify(recordDao).insertRecord(any(Record.class));
        verify(tagDao,times(3)).findTagListById(anyList());
        verify(recordTagDao).batchInsertRecordTag(eq(tagList), anyLong());
        verify(recordDao).findRecordById(id);
    }
    @Test
    void testUpdateRecord(){
        val id = 100L;
        val userId = 200L;
        val amount = new BigDecimal(100.25);
        val tagId = 1L;
        val tagInCommon = Tag.builder()
            .id(tagId)
            .userId(userId)
            .description("playing")
            .status(1)
            .build();
        val tagInCommon2 = Tag.builder()
            .id(2L)
            .userId(userId)
            .description("playing2")
            .status(1)
            .build();
        val tagList = ImmutableList.of(tagInCommon);
        val updateList = ImmutableList.of(tagInCommon2);
        val recordConvert = Record.builder()
            .id(id)
            .userId(userId)
            .status(1)
            .tagList(updateList)
            .amount(amount)
            .note("Playing game2")
            .category(0)
            .build();
        val recordInDB = Record.builder()
            .id(id)
            .userId(userId)
            .status(1)
            .tagList(tagList)
            .amount(amount)
            .note("Playing game")
            .category(0)
            .build();
        val recordIncommen = p2CConverter.convert(recordConvert);
        when(recordDao.findRecordById(anyLong())).thenReturn(recordInDB).thenReturn(recordConvert);
        doReturn(tagList).when(tagDao).findTagListById(anyList());
        doNothing().when(recordTagDao).deleteRecordTagByRecordId(anyLong());
        doNothing().when(recordTagDao).batchInsertRecordTag(anyList(),anyLong());
        doNothing().when(recordDao).updateRecord(any(Record.class));
        //Act
        val result = recordService.updateRecord(recordIncommen);
        //Assert
        assertThat(result).isNotNull()
            .hasFieldOrPropertyWithValue("id", id)
            .hasFieldOrPropertyWithValue("userId", userId)
            .hasFieldOrPropertyWithValue("note", "Playing game2")
            .hasFieldOrPropertyWithValue("category", "OUTCOME");
        verify(recordDao,times(2)).findRecordById(any());
        verify(tagDao).findTagListById(anyList());
        verify(recordTagDao).deleteRecordTagByRecordId(id);
        verify(recordTagDao).batchInsertRecordTag(anyList(),anyLong());
        //错误测试
        val errorTag = Tag.builder()
            .id(tagId)
            .userId(10L)
            .description("playing")
            .status(1)
            .build();
        val errorTagList =ImmutableList.of(errorTag);
        val errorRecord = Record.builder()
            .id(id)
            .userId(2L)
            .status(1)
            .tagList(tagList)
            .amount(amount)
            .note("Playing game")
            .category(0)
            .build();
        doReturn(errorRecord).when(recordDao).findRecordById(any());
        assertThrows(InvalidParameterException.class,()->recordService.updateRecord(recordIncommen));
        doReturn(null).when(recordDao).findRecordById(any());
        assertThrows(InvalidParameterException.class,()->recordService.updateRecord(recordIncommen));
    }
}

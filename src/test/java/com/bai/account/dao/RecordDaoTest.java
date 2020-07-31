package com.bai.account.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.bai.account.dao.mapper.RecordMapper;
import com.bai.account.model.persistence.Record;
import com.bai.account.model.persistence.Tag;

import java.math.BigDecimal;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecordDaoTest {
    @Mock
    private RecordMapper mapper;
    @InjectMocks
    private RecordDaoImpl recordDao;
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
        doReturn(recordInDB).when(mapper).findRecordById(any());
        //Act
        val result = recordDao.findRecordById(id);
        //Assert
        assertEquals(result,recordInDB);
        verify(mapper).findRecordById(id);
    }
    @Test
    void testCreate(){
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
        doNothing().when(mapper).insertRecord(recordInDB);
        //Act&Assert
        recordDao.insertRecord(recordInDB);
        verify(mapper).insertRecord(recordInDB);
    }
    @Test
    void testUpdateRecord(){
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
        doNothing().when(mapper).updateRecord(recordInDB);
        //Act&Assert
        recordDao.updateRecord(recordInDB);
        verify(mapper).updateRecord(recordInDB);
    }
}

package com.bai.account.dao;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.bai.account.dao.mapper.RecordTagMapper;
import com.bai.account.model.persistence.Tag;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecordTagDaoTest {
    @Mock
    private RecordTagMapper recordTagMapper;
    @InjectMocks
    private RecordTagDaoImpl recordTagDao;
    @Test
    void testBatchInsertRecordTag(){
        val description = "eating";
        Integer status = 1;
        Long userId = 10L;
        val recordId = 100L;
        val tag = Tag.builder()
            .description(description)
            .status(status)
            .userId(userId)
            .build();
        val tagList = ImmutableList.of(tag);
        doReturn(1).when(recordTagMapper).batchInsertRecordTag(any());
        recordTagDao.batchInsertRecordTag(tagList,recordId);
        verify(recordTagMapper).batchInsertRecordTag(any());
    }
    @Test
    void testDeleteRecordTagByRecordId(){
        val id = 100L;
        doReturn(1).when(recordTagMapper).deleteRecordTagByRecordId(eq(id));
        recordTagDao.deleteRecordTagByRecordId(id);
        verify(recordTagMapper).deleteRecordTagByRecordId(eq(id));
    }
}

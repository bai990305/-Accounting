package com.bai.account.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.bai.account.dao.mapper.TagMapper;
import com.bai.account.dao.mapper.UserInfoMapper;
import com.bai.account.model.persistence.Tag;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TagDaoTest {
    @Mock
    private TagMapper mapper;
    @InjectMocks
    private TagDaoImpl dao;
    @Test
    void testFindTagByDescription(){
        //Arrange
        val id = 100L;
        val description = "bai";
        val userId = 100L;
        val tag = Tag.builder()
            .id(id)
            .description(description)
            .status(1)
            .userId(userId)
            .build();
        doReturn(tag).when(mapper).findTagByDescription(any(),any());
        //Act
        val result = dao.findTagByDescription(description,userId);
        //Assert
        assertEquals(tag,result);
        verify(mapper).findTagByDescription(any(),any());
    }
    @Test
    void testCreateTag(){
        //Arrange
        val id = 100L;
        val description = "bai";
        val userId = 100L;
        val tag = Tag.builder()
            .id(id)
            .description(description)
            .status(1)
            .userId(userId)
            .build();
        doNothing().when(mapper).insertTag(any(Tag.class));
        //Act
        dao.createTag(tag);
        //Assert
        verify(mapper).insertTag(any(Tag.class));
    }
    @Test
    void testFindTagById(){
        //Arrange
        val id = 100L;
        val description = "bai";
        val userId = 100L;
        val tag = Tag.builder()
            .id(id)
            .description(description)
            .status(1)
            .userId(userId)
            .build();
        doReturn(tag).when(mapper).findTagById(any());
        //Act
        val result = dao.findTagById(id);
        //Assert
        assertEquals(tag,result);
        verify(mapper).findTagById(any());
    }
    @Test
    void testUpdateTag(){
        //Arrange
        val id = 100L;
        val description = "bai";
        val userId = 100L;
        val tag = Tag.builder()
            .id(id)
            .description(description)
            .status(1)
            .userId(userId)
            .build();
        doNothing().when(mapper).updateTag(any(Tag.class));
        //Act
        dao.updateTag(tag);
        //Assert
        verify(mapper).updateTag(any(Tag.class));
    }
}

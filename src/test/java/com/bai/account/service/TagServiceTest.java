package com.bai.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.bai.account.converter.p2c.TagP2CConverter;
import com.bai.account.dao.TagDao;
import com.bai.account.exception.InvalidParameterException;
import com.bai.account.exception.ResourceNotFoundException;
import com.bai.account.model.persistence.Tag;
import com.bai.account.service.impl.TagServiceImpl;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TagServiceTest {
    @Mock
    private TagDao tagDao;
    private TagService service;
    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        service = new TagServiceImpl(tagDao, new TagP2CConverter());
    }
    @Test
    void testFindTagById(){
        //Arrange
        val id = 100L;
        val description = "bai";
        val userId = 100L;
        val status = "ENABLE";
        val tag = Tag.builder()
            .description(description)
            .id(id)
            .userId(userId)
            .status(1)
            .build();
        doReturn(tag).when(tagDao).findTagById(any());
        //Act
        val result = tagDao.findTagById(id);
        //Arrange
        assertEquals(tag,result);
        //verify(tagDao).findTagById(any());
        doReturn(null).when(tagDao).findTagById(any());
        //错误测试
        assertThrows(ResourceNotFoundException.class,()->service.findTagById(id));
        verify(tagDao,times(2)).findTagById(any());
    }
    @Test
    void testCreateTag(){
        //Arrange
        val id = 100L;
        val description = "bai";
        val userId = 100L;
        val status = "ENABLE";
        val tag = Tag.builder()
            .description(description)
            .id(id)
            .userId(userId)
            .status(1)
            .build();
        doReturn(null).when(tagDao).findTagByDescription(description,userId);
        doNothing().when(tagDao).createTag(any(Tag.class));
        //Act
        com.bai.account.model.common.Tag result = service.createTag(description, userId);
        //Assert
        verify(tagDao).createTag(any(Tag.class));
        //verify(tagDao).findTagByDescription(description,userId);
        //错误测试
        doReturn(tag).when(tagDao).findTagByDescription(description,userId);
        assertThrows(InvalidParameterException.class,()->service.createTag(description, userId));
        verify(tagDao,times(2)).findTagByDescription(any(),any());

    }
    @Test
    void testUpdateTag(){
        val tagId = 100L;
        val userId = 1900L;
        val description = "playing";
        val tag = com.bai.account.model.common.Tag.builder()
            .id(tagId)
            .description(description)
            .userId(userId)
            .status("ENABLE")
            .build();
        val tagInDb = Tag.builder()
            .id(tagId)
            .description(description)
            .userId(userId)
            .status(1)
            .build();
        val tagInDb2 = Tag.builder()
            .id(tagId)
            .description(description)
            .userId(3L)
            .status(1)
            .build();
        doReturn(tagInDb).when(tagDao).findTagById(tagId);
        doNothing().when(tagDao).updateTag(tagInDb);
        // Act
        val result = service.updateTag(tag);
        // Assert
        assertEquals(tag, result);
        //verify(tagDao, times(4)).findTagById(tagId);
        verify(tagDao).updateTag(any(Tag.class));
        //错误测试
        doReturn(null).when(tagDao).findTagById(tagId);
        assertThrows(ResourceNotFoundException.class,()->service.updateTag(tag));
        doReturn(tagInDb2).when(tagDao).findTagById(tagId);
        assertThrows(InvalidParameterException.class,()->service.updateTag(tag));
        verify(tagDao, times(4)).findTagById(tagId);
    }
}

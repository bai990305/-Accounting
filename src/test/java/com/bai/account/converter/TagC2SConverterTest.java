package com.bai.account.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import com.bai.account.converter.c2s.TagC2SConverter;
import com.bai.account.model.common.Tag;


import lombok.val;
import org.junit.jupiter.api.Test;

public class TagC2SConverterTest {
    private TagC2SConverter Converter = new TagC2SConverter();
    @Test
    void testDoForward(){
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
        //Act&&Assert
        val result = Converter.convert(tag);
        assertThat(result).isNotNull().hasFieldOrPropertyWithValue("id",id)
        .hasFieldOrPropertyWithValue("description",description)
        .hasFieldOrPropertyWithValue("userId",userId);
    }
    @Test
    void testDoBackward(){
        //Arrange
        val id = 100L;
        val description = "bai";
        val userId = 100L;
        val status = "ENABLE";
        val tag = com.bai.account.model.service.Tag.builder()
            .id(id)
            .description(description)
            .userId(userId)
            .status(status)
            .build();
        //Act&&Assert
        val result = Converter.reverse().convert(tag);
        assertThat(result).isNotNull().hasFieldOrPropertyWithValue("id",id)
            .hasFieldOrPropertyWithValue("description",description)
            .hasFieldOrPropertyWithValue("userId",userId);
    }
}

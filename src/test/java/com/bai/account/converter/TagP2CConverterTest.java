package com.bai.account.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.bai.account.converter.p2c.TagP2CConverter;
import com.bai.account.model.persistence.Tag;

import java.time.LocalDateTime;

import lombok.val;
import org.junit.jupiter.api.Test;

public class TagP2CConverterTest {
    private TagP2CConverter converter = new TagP2CConverter();
    @Test
    void testDoForward(){
        //Arrange
        val id = 100L;
        val description = "bai";
        val userId = 100L;
        val status = "ENABLE";
        val createTime = LocalDateTime.now();
        val tagInCommon = Tag.builder()
            .id(id)
            .description(description)
            .userId(userId)
            .status(1)
            .createTime(createTime)
            .build();
        //Act&&Assert
        val result = converter.convert(tagInCommon);
        assertThat(result).isNotNull().hasFieldOrPropertyWithValue("id",id)
            .hasFieldOrPropertyWithValue("description",description)
            .hasFieldOrPropertyWithValue("userId",userId)
            .hasFieldOrPropertyWithValue("status", "ENABLE");
    }
    @Test
    void testDoBackward(){
        //Arrange
        val id = 100L;
        val description = "bai";
        val userId = 100L;
        val status = "ENABLE";
        val tagInCommon = com.bai.account.model.common.Tag.builder()
            .id(id)
            .description(description)
            .userId(userId)
            .status(status)
            .build();
        //Act&&Assert
        val result = converter.reverse().convert(tagInCommon);
        assertThat(result).isNotNull().hasFieldOrPropertyWithValue("id",id)
            .hasFieldOrPropertyWithValue("description",description)
            .hasFieldOrPropertyWithValue("userId",userId)
            .hasFieldOrPropertyWithValue("status", 1);
    }
}

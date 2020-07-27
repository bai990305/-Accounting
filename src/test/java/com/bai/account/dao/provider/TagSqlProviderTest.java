package com.bai.account.dao.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bai.account.model.persistence.Tag;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.jupiter.api.Test;

public class TagSqlProviderTest {

    private TagSqlProvider tagSqlProvider = new TagSqlProvider();

    @Test
    void testUpdateTagSQL() {
        // Arrange
        val description = "eating";
        Integer status = 1;
        Long userId = 10L;
        val tag = Tag.builder()
            .description(description)
            .status(status)
            .userId(userId)
            .build();

        // Act
        val result = tagSqlProvider.updateTag(tag);
        // Assert
        String expectedSql = "UPDATE hcas_tag\n"
            + "SET description = #{description}, status = #{status}, user_id = #{userId}\n"
            + "WHERE (id = #{id})";
        assertEquals(expectedSql, result);
    }
}
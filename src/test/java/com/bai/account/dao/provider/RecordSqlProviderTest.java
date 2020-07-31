package com.bai.account.dao.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bai.account.model.persistence.Record;

import java.math.BigDecimal;

import lombok.val;
import org.junit.jupiter.api.Test;

public class RecordSqlProviderTest {
    private RecordSqlProvider recordSqlProvider = new RecordSqlProvider();
    @Test
    void testUpdateRecord() {
        // Arrange
        val record = Record.builder()
            .id(1L)
            .amount(new BigDecimal("100.05"))
            .category(1)
            .note("xxxx")
            .userId(100L)
            .status(0)
            .build();

        // Act
        val result = recordSqlProvider.updateRecord(record);
        String expectedSql = "UPDATE hcas_record\n"
            + "SET amount = #{amount}, category = #{category}, note = #{note}, status = #{status}, user_id = #{userId}\n"
            + "WHERE (id = #{id})";
        assertEquals(expectedSql, result);
    }
}

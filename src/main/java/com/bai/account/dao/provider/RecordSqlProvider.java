package com.bai.account.dao.provider;

import com.bai.account.model.persistence.Record;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;



@Slf4j
public class RecordSqlProvider {
    /**
     * 更新record 的provider.
     * @param record 更新传入的record
     * @return
     */
    public String updateRecord(Record record) {
        return new SQL() {
            {
                UPDATE("hcas_record");
                if (record.getAmount() != null) {
                    SET("amount = #{amount}");
                }
                if (record.getCategory() != null) {
                    SET("category = #{category}");
                }
                if (record.getNote() != null) {
                    SET("note = #{note}");
                }
                if (record.getStatus() != null) {
                    SET("status = #{status}");
                }
                if (record.getUserId() != null) {
                    SET("user_id = #{userId}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
    }
}

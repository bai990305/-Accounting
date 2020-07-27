package com.bai.account.dao.provider;

import com.bai.account.model.persistence.Tag;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

@Slf4j
public class TagSqlProvider {
    /**
     * Update tag 所用的provider.
     *
     * @param tag the tag item need to update.
     * @return the sql to execute.
     */
    public String updateTag(final Tag tag) {
        // Dynamic SQL
        return new SQL() {
            {
                UPDATE("hcas_tag");
                if (tag.getDescription() != null) {
                    SET("description = #{description}");
                }
                if (tag.getStatus() != null) {
                    SET("status = #{status}");
                }
                if (tag.getUserId() != null) {
                    SET("user_id = #{userId}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
    }

}

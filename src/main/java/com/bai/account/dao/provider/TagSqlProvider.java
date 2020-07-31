package com.bai.account.dao.provider;

import com.bai.account.model.persistence.Tag;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
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

    /**
     * Select tag 所用的Provider.
     * @param tagIds 传入的tag id的集合
     * @return the sql to execute.
     */
    public String selectTag(@Param("id") final List<Long> tagIds) {
        return new SQL() {
            {
                SELECT("id", "description", "user_id", "status");
                FROM("hcas_tag");
                WHERE(String.format("id in ('%s')", Joiner.on("','").skipNulls().join(tagIds)));
            }
        }.toString();
    }
}

package com.bai.account.dao.mapper;

import com.bai.account.model.persistence.RecordTag;
import com.bai.account.model.persistence.Tag;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface RecordTagMapper {
    @Select("select * from hcas_tag where id in (select tag_id from record_tag where record_id = #{recordId})")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "description", property = "description"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "status", property = "status"),
        @Result(column = "create_time", property = "createTime"),
        @Result(column = "update_time", property = "updateTime"),
    })
    List<Tag> findTagListByRecordId(@Param("id")Long recordId);
    @Insert({"<script>",
        "INSERT INTO record_tag(record_id, tag_id, status) VALUES ",
        "<foreach item='item' index='index' collection='recordTagMappings'",
        "open='(' separator = '),(' close=')'>",
        "#{item.recordId}, #{item.tagId}, #{item.status}",
        "</foreach>",
        "</script>"})
    int batchInsertRecordTag(@Param("recordTagMappings") List<RecordTag> recordTagMappings);
    @Delete("delete from record_tag where record_id = #{id}")
    int deleteRecordTagByRecordId(Long id);
}

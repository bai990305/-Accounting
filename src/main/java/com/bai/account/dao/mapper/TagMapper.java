package com.bai.account.dao.mapper;

import com.bai.account.dao.provider.TagSqlProvider;
import com.bai.account.model.persistence.Tag;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

@Mapper
public interface TagMapper {
    @Select("select * from hcas_tag where user_id=#{userId} and description=#{description}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "description", property = "description"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "status", property = "status"),
        @Result(column = "create_time", property = "createTime"),
        @Result(column = "update_time", property = "updateTime"),
    })
    Tag findTagByDescription(String description, Long userId);
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("INSERT INTO hcas_tag(description, user_id, status ,create_time) "
        + "VALUES (#{description}, #{userId}, #{status}, #{createTime})")
    void insertTag(Tag newTag);
    @Select("select * from hcas_tag where id=#{id}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "description", property = "description"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "status", property = "status"),
        @Result(column = "create_time", property = "createTime"),
        @Result(column = "update_time", property = "updateTime"),
    })
    Tag findTagById(Long id);
    @UpdateProvider(value = TagSqlProvider.class,method = "updateTag")
    @Options(resultSets = "id, description, user_id, status, create_time, update_time")
    void updateTag(Tag updateTag);
    @SelectProvider(value = TagSqlProvider.class,method = "selectTag")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "description", property = "description"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "status", property = "status")
    })
    List<Tag> findTagListById(@Param("id")List<Long> tagIds);
}

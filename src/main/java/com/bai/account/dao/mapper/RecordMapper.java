package com.bai.account.dao.mapper;

import com.bai.account.dao.provider.RecordSqlProvider;
import com.bai.account.model.persistence.Record;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

@Mapper
public interface RecordMapper {
    @Select("select * from hcas_record where id = #{id}")
    @Results({
        @Result(id = true,property = "id",column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "amount", column = "amount"),
        @Result(property = "category", column = "category"),
        @Result(property = "status", column = "status"),
        @Result(property = "tagList", javaType = List.class, column = "id",
            many = @Many(select = "com.bai.account.dao.mapper."
                + "RecordTagMapper.findTagListByRecordId"))
    })
    Record findRecordById(@Param("id")Long id);
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO hcas_record(user_id, amount, note, category, status, create_time) "
        + "VALUES (#{userId}, #{amount}, #{note}, #{category}, #{status}, #{createTime})")
    void insertRecord(Record record);
    @UpdateProvider(type = RecordSqlProvider.class, method = "updateRecord")
    void updateRecord(Record record);
}

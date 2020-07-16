package com.bai.account.dao.mapper;

import com.bai.account.model.persistence.UserInfo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoMapper {

    @Select("select * from hcas_userinfo where id = #{id}")
    UserInfo findUserInfoById(@Param("id") Long id);

    @Select("select * from hcas_userinfo where username = #{username}")
    UserInfo findUserInfoByUserName(@Param("username")String username);

}

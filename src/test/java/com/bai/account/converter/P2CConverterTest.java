package com.bai.account.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.bai.account.converter.p2c.UserInfoP2CConverter;
import com.bai.account.model.persistence.UserInfo;

import java.time.LocalDateTime;

import lombok.val;
import org.junit.jupiter.api.Test;

public class P2CConverterTest {

    private UserInfoP2CConverter converter =new UserInfoP2CConverter();

    @Test
    public void testDoForward() {
        //Arrange
        val id = 100L;
        val username = "bai";
        val password = "bai";
        val CreateDate = LocalDateTime.now();
        val userinfo = UserInfo.builder()
            .createTime(CreateDate)
            .id(id)
            .password(password)
            .username(username)
            .build();
        //Act
        val result = converter.convert(userinfo);
        //Assert
        assertThat(result).isNotNull()
            .hasFieldOrPropertyWithValue("id", id)
            .hasFieldOrPropertyWithValue("username", username)
            .hasFieldOrPropertyWithValue("password", password);
    }

    @Test
    public void testDoBackward(){
        //Arrange
        val id = 100L;
        val username = "bai";
        val password = "bai";
        val userinfo = com.bai.account.model.common.UserInfo.builder()
            .id(id)
            .password(password)
            .username(username)
            .build();
        //Act
        val result = converter.reverse().convert(userinfo);
        //Assert
        assertThat(result).isNotNull()
            .hasFieldOrPropertyWithValue("id", id)
            .hasFieldOrPropertyWithValue("username", username)
            .hasFieldOrPropertyWithValue("password", password);
    }
}


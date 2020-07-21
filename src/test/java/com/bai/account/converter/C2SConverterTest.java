package com.bai.account.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.bai.account.converter.c2s.UserInfoC2SConverter;

import lombok.val;
import org.junit.jupiter.api.Test;

public class C2SConverterTest {

    private UserInfoC2SConverter converter = new UserInfoC2SConverter();

    @Test
    public void testDoForward(){
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
        val result = converter.convert(userinfo);
        //Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", id)
                          .hasFieldOrPropertyWithValue("username", username);
    }
    @Test
    void testDoBackward() {
        // Arrange
        val userId = 100L;
        val username = "hardcore";
        val password = "hardcore";

        val userInfoInCommon = com.bai.account.model.service.UserInfo.builder()
            .id(userId)
            .username(username)
            .password(password)
            .build();

        // Act
        val result = converter.reverse().convert(userInfoInCommon);
        // Assert
        assertThat(result).isNotNull()
            .hasFieldOrPropertyWithValue("id", userId)
            .hasFieldOrPropertyWithValue("username", username)
            .hasFieldOrPropertyWithValue("password", password);

    }
}


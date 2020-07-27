package com.bai.account.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import com.bai.account.model.common.UserInfo;
import com.bai.account.service.UserInfoService;

import java.util.UUID;

import lombok.val;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRealmTest {

    @Mock
    private UserInfoService service;

    @Mock
    private HashedCredentialsMatcher matcher;

    @InjectMocks
    private UserRealm userRealm;


    @Test
    void testDoGetAuthenticationInfo() {
        // Arrange
        val username = "hardcore";
        val password = "hardcore";
        val encodePassword = UUID.randomUUID().toString();
        val salt = UUID.randomUUID().toString();

        val token = new UsernamePasswordToken(username, password);

        val userInfo = UserInfo.builder()
                               .id(1L)
                               .username(username)
                               .password(encodePassword)
                               .salt(salt)
                               .build();

        doReturn(userInfo).when(service).findUserInfoByUserName(username);
        // Act
        val result = userRealm.doGetAuthenticationInfo(token);
        // Assert
        assertThat(result).isNotNull();
    }
}
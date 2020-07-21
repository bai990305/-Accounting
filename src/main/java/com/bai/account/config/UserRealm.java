package com.bai.account.config;

import com.bai.account.model.common.UserInfo;
import com.bai.account.service.UserInfoService;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserRealm extends AuthorizingRealm {
    private UserInfoService userInfoService;
    @Autowired
    public UserRealm(UserInfoService userInfoService,HashedCredentialsMatcher matcher) {
        super(matcher);
        this.userInfoService = userInfoService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
        throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        UserInfo userInfo = userInfoService.findUserInfoByUserName(username);
        log.info("salt is " + userInfo.getSalt());
        return new SimpleAuthenticationInfo(userInfo.getUsername(),userInfo.getPassword(),
            ByteSource.Util.bytes(userInfo.getSalt()),this.getName());
    }
}

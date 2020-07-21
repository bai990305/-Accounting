package com.bai.account.controller;

import com.bai.account.converter.c2s.UserInfoC2SConverter;
import com.bai.account.model.service.UserInfo;
import com.bai.account.service.UserInfoService;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/user")
public class UserController {
    private final UserInfoService service;
    private final UserInfoC2SConverter userInfoC2SConverter;
    @Autowired
    public UserController(UserInfoService service,UserInfoC2SConverter userInfoC2SConverter) {
        this.service = service;
        this.userInfoC2SConverter = userInfoC2SConverter;
    }

    /**
     * .
     * 注册功能
     * @param userInfo 传入的用户名密码.
     * @return 注册成功后返回的用不明密码.
     */
    @PostMapping(produces = "application/json",consumes = "application/json")
    public ResponseEntity<UserInfo> register(@RequestBody UserInfo userInfo) {
        val userInfoReturn = service.register(userInfo.getUsername(),userInfo.getPassword());
        System.out.println(userInfoReturn);
        UserInfo user = userInfoC2SConverter.convert(userInfoReturn);
        assert user != null;
        return ResponseEntity.ok(user);
    }
}

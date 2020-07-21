package com.bai.account.controller;

import com.bai.account.model.service.UserInfo;
import com.bai.account.service.UserInfoService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/session")
public class SessionController {
    private UserInfoService service;

    public SessionController(UserInfoService service) {
        this.service = service;
    }

    @PostMapping(produces = "application/json",consumes = "application/json")
    public String login(@RequestBody UserInfo userInfo) {
        service.login(userInfo.getUsername(),userInfo.getPassword());
        return "success";
    }
}


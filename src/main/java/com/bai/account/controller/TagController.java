package com.bai.account.controller;

import com.bai.account.converter.c2s.TagC2SConverter;
import com.bai.account.exception.InvalidParameterException;
import com.bai.account.exception.ResourceNotFoundException;
import com.bai.account.model.service.Tag;
import com.bai.account.service.TagService;
import com.bai.account.service.UserInfoService;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/tag")
public class TagController {
    private final TagService service;
    private final TagC2SConverter c2SConverter;
    private final UserInfoService userInfoService;
    /**
     * Constructor for TagController.
     *
     * @param service      tag manager
     * @param userInfoService user info manager
     * @param c2SConverter tag converter
     */
    @Autowired
    public TagController(TagService service, TagC2SConverter c2SConverter, UserInfoService userInfoService) {
        this.service = service;
        this.c2SConverter = c2SConverter;
        this.userInfoService = userInfoService;
    }

    /**
     * 创建tag用.
     * @param tag 传入的tag类.
     * @return tag类 service型.
     */
    @PostMapping(produces = "application/json",consumes = "application/json")
        public Tag createTag(@RequestBody Tag tag) {
        if (tag.getDescription() == null || tag.getDescription().isEmpty() || tag.getUserId() == null) {
            throw new InvalidParameterException("The description and user id must be not null or empty.");
        }
        val resource = service.createTag(tag.getDescription(),tag.getUserId());
        return c2SConverter.convert(resource);
    }
    @GetMapping(path = "/{id}",produces = "application/json",consumes = "application/json")
    Tag getTagByTagId(@PathVariable("id") Long id) {
        if (id == null || id <= 0L) {
            throw new InvalidParameterException("The tagId must be not empty and positive.");
        }
        val result = service.findTagById(id);
        return c2SConverter.convert(result);
    }

    /**
     * 更新tag.
     * @param id tag的id.
     * @param tag 更新的tag类.
     * @return
     */
    @PutMapping(path = "/{id}",produces = "application/json",consumes = "application/json")
    public Tag updateTag(@PathVariable("id") Long id, @RequestBody Tag tag) {
        if (id == null || id <= 0L) {
            throw new InvalidParameterException("The tagId must be not empty and positive.");
        }
        if (tag.getUserId() == null || tag.getUserId() <= 0L) {
            throw new InvalidParameterException("The userId must be not empty and positive.");
        }
        val userInfo = userInfoService.findUserInfoById(tag.getUserId());
        if (userInfo == null) {
            throw new ResourceNotFoundException("The userId not found" + tag.getUserId());
        }
        String status = tag.getStatus();
        if (status != null && !"ENABLE".equals(status) && !"DISABLE".equals(status)) {
            throw new InvalidParameterException(String.format("The status [%s] to update is invalid status", status));
        }
        tag.setId(id);
        val convertTag = c2SConverter.reverse().convert(tag);
        val resultTag = service.updateTag(convertTag);
        return c2SConverter.convert(resultTag);
    }
}


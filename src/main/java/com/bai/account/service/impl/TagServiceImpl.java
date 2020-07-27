package com.bai.account.service.impl;

import com.bai.account.converter.p2c.TagP2CConverter;
import com.bai.account.dao.TagDao;
import com.bai.account.exception.InvalidParameterException;
import com.bai.account.exception.ResourceNotFoundException;
import com.bai.account.model.common.Tag;
import com.bai.account.service.TagService;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private static final int ENABLE_STATUS = 1;
    private final TagDao tagDao;
    private final TagP2CConverter tagP2CConverter;
    @Autowired
    public TagServiceImpl(TagDao tagDao, TagP2CConverter tagP2CConverter) {
        this.tagDao = tagDao;
        this.tagP2CConverter = tagP2CConverter;
    }

    @Override
    public Tag createTag(String description, Long userId) {
        Optional.ofNullable(tagDao.findTagByDescription(description, userId))
            .ifPresent((tag) -> {
                throw new InvalidParameterException(
                    String.format("The related tag with description [%s] has been created", description));
            });
        val newTag = com.bai.account.model.persistence.Tag.builder()
            .description(description)
            .userId(userId)
            .status(ENABLE_STATUS)
            .createTime(LocalDateTime.now())
            .build();
        tagDao.createTag(newTag);
        return tagP2CConverter.convert(newTag);
    }

    @Override
    public Tag findTagById(Long id) {
        return Optional.ofNullable(tagDao.findTagById(id))
            .map(tagP2CConverter::convert)
            .orElseThrow(
                () -> new ResourceNotFoundException(String.format("The related tag id [%s] was not found", id)));
    }
    @Override
    public Tag updateTag(Tag convertTag) {
        val updateTag = tagP2CConverter.reverse().convert(convertTag);
        assert updateTag != null;
        val tagInDb = Optional.ofNullable(tagDao.findTagById(updateTag.getId()))
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("The related tag id [%s] was not found", convertTag.getId())));
        if (!convertTag.getUserId().equals(tagInDb.getUserId())) {
            throw new InvalidParameterException(
                String.format("The tag id [%s] doesn't belong to user id: [%s]",
                    convertTag.getId(), convertTag.getUserId()));
        }
        tagDao.updateTag(updateTag);
        return findTagById(convertTag.getId());
    }
}

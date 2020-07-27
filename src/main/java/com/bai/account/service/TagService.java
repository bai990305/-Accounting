package com.bai.account.service;

import com.bai.account.model.common.Tag;

public interface TagService {
    Tag createTag(String description, Long userId);

    Tag findTagById(Long id);

    Tag updateTag(Tag convertTag);
}

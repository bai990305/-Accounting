package com.bai.account.dao;

import com.bai.account.model.persistence.Tag;
@SuppressWarnings("checkstyle:RegexpSingleline")
public interface TagDao {
    Tag findTagByDescription(String description, Long userId);

    void createTag(Tag newTag);

    Tag findTagById(Long id);

    void updateTag(Tag updateTag);
}

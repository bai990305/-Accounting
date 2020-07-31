package com.bai.account.dao;

import com.bai.account.model.persistence.Tag;

import java.util.List;

@SuppressWarnings("checkstyle:RegexpSingleline")
public interface TagDao {
    Tag findTagByDescription(String description, Long userId);

    void createTag(Tag newTag);

    Tag findTagById(Long id);

    void updateTag(Tag updateTag);

    List<Tag> findTagListById(List<Long> tagIds);
}

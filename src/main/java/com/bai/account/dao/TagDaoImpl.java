package com.bai.account.dao;

import com.bai.account.dao.mapper.TagMapper;
import com.bai.account.model.persistence.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class TagDaoImpl implements TagDao {
    private final TagMapper tagMapper;
    @Autowired
    public TagDaoImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public Tag findTagByDescription(String description, Long userId) {
        return tagMapper.findTagByDescription(description,userId);
    }

    @Override
    public void createTag(Tag newTag) {
        tagMapper.insertTag(newTag);
    }

    @Override
    public Tag findTagById(Long id) {
        return tagMapper.findTagById(id);
    }

    @Override
    public void updateTag(Tag updateTag) {
        tagMapper.updateTag(updateTag);
    }

    @Override
    public List<Tag> findTagListById(List<Long> tagIds) {
        return tagMapper.findTagListById(tagIds);
    }
}

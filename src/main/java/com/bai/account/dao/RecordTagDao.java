package com.bai.account.dao;

import com.bai.account.model.persistence.Tag;

import java.util.List;

public interface RecordTagDao {
    void batchInsertRecordTag(List<Tag> tags, Long recordId);

    void deleteRecordTagByRecordId(Long id);
}

package com.bai.account.dao;

import com.bai.account.dao.mapper.RecordTagMapper;
import com.bai.account.model.persistence.RecordTag;
import com.bai.account.model.persistence.Tag;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
@Repository
@Slf4j
public class RecordTagDaoImpl implements RecordTagDao {
    private RecordTagMapper recordTagMapper;
    @Autowired
    public RecordTagDaoImpl(RecordTagMapper recordTagMapper) {
        this.recordTagMapper = recordTagMapper;
    }

    @Override
    public void batchInsertRecordTag(List<Tag> tags, Long recordId) {
        val recordTagMappingList = tags.stream()
            .map(tag -> RecordTag.builder()
                .status(1)
                .tagId(tag.getId())
                .recordId(recordId)
                .build())
            .collect(Collectors.toList());
        val rows = recordTagMapper.batchInsertRecordTag(recordTagMappingList);
        log.debug("The row inserted: {}", rows);
    }

    @Override
    public void deleteRecordTagByRecordId(Long id) {
        val rows = recordTagMapper.deleteRecordTagByRecordId(id);
        log.debug("The row deleted: {}", rows);
    }
}

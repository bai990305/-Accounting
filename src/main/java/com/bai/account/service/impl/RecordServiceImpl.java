package com.bai.account.service.impl;

import com.bai.account.converter.p2c.RecordP2CConverter;
import com.bai.account.dao.RecordDao;
import com.bai.account.dao.RecordTagDao;
import com.bai.account.dao.TagDao;
import com.bai.account.exception.InvalidParameterException;
import com.bai.account.exception.ResourceNotFoundException;
import com.bai.account.model.common.Record;
import com.bai.account.model.persistence.Tag;
import com.bai.account.service.RecordService;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecordServiceImpl implements RecordService {
    private RecordDao recordDao;
    private RecordP2CConverter p2CConverter;
    private TagDao tagDao;
    private RecordTagDao recordTagDao;

    /**
     * 构造方法.
     * @param recordDao recordDao.
     * @param p2CConverter converter for record p2c.
     * @param tagDao tagDao.
     */
    @Autowired
    public RecordServiceImpl(RecordDao recordDao, RecordP2CConverter p2CConverter,
                             TagDao tagDao,RecordTagDao recordTagDao) {
        this.recordDao = recordDao;
        this.p2CConverter = p2CConverter;
        this.tagDao = tagDao;
        this.recordTagDao = recordTagDao;
    }
    @Override
    public Record findRecordById(Long id) {
        return Optional.ofNullable(recordDao.findRecordById(id))
            .map(p2CConverter::convert)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("The id %s is not found",id)));
    }

    @Override
    public Record createRecord(Record newRecord) {
        val record = p2CConverter.reverse().convert(newRecord);
        assert record != null;
        val tagIds = record.getTagList()
            .stream()
            .map(Tag::getId)
            .collect(Collectors.toList());
        val tags = tagDao.findTagListById(tagIds);
        if (tags.isEmpty()) {
            throw new InvalidParameterException((String.format("The tag list %s are not existed.", tagIds)));
        }
        tags.forEach(tag -> {
            if (!tag.getUserId().equals(record.getUserId())) {
                throw new InvalidParameterException("The tag is not matched for user");
            }
        });
        recordDao.insertRecord(record);
        recordTagDao.batchInsertRecordTag(tags,record.getId());
        return findRecordById(record.getId());
    }

    @Override
    public Record updateRecord(Record newRecord) {
        val recordById = recordDao.findRecordById(newRecord.getId());
        if (recordById == null) {
            throw new InvalidParameterException("The record id not exist");
        }
        val record = p2CConverter.reverse().convert(newRecord);
        assert record != null;
        if (!record.getUserId().equals(recordById.getUserId())) {
            throw new InvalidParameterException(
                String.format("The record id [%s] doesn't belong to user id: [%s]",
                    record.getId(), record.getUserId()));
        }
        if (record.getTagList() != null && !record.getTagList().equals(recordById.getTagList())) {
            val tagIds = record.getTagList().stream().map(Tag::getId).collect(Collectors.toList());
            val tags = tagDao.findTagListById(tagIds);
            tags.stream()
                .filter(tag -> !tag.getUserId().equals(newRecord.getUserId()))
                .findAny()
                .ifPresent(tag -> {
                    throw new InvalidParameterException(
                        String.format("The tags id [%s] doesn't belong to user id: [%s]",
                            tag.getId(), newRecord.getUserId()));
                });
            recordTagDao.deleteRecordTagByRecordId(newRecord.getId());
            recordTagDao.batchInsertRecordTag(tags,newRecord.getId());
        }
        recordDao.updateRecord(record);
        return findRecordById(newRecord.getId());
    }
}

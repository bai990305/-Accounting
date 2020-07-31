package com.bai.account.dao;

import com.bai.account.dao.mapper.RecordMapper;
import com.bai.account.model.persistence.Record;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RecordDaoImpl implements RecordDao {
    @Autowired
    private RecordMapper mapper;
    @Override
    public Record findRecordById(Long id) {
        val result = mapper.findRecordById(id);
        log.debug("Record in getRecordByRecordId: {}",result);
        return result;
    }

    @Override
    public void insertRecord(Record record) {
        record.setStatus(1);
        log.debug("Record in RecordDaoImpl: {}", record);
        mapper.insertRecord(record);
        log.debug("Record in RecordDaoImpl after inserting: {}", record);
    }

    @Override
    public void updateRecord(Record record) {
        record.setStatus(1);
        mapper.updateRecord(record);
    }
}

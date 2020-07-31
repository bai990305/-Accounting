package com.bai.account.dao;

import com.bai.account.model.persistence.Record;

public interface RecordDao {
    Record findRecordById(Long id);

    void insertRecord(Record record);

    void updateRecord(Record record);
}

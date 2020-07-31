package com.bai.account.service;

import com.bai.account.model.common.Record;

public interface RecordService {
    Record findRecordById(Long id);

    Record createRecord(Record newRecord);

    Record updateRecord(Record newRecord);
}

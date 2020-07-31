package com.bai.account.controller;

import com.bai.account.converter.c2s.RecordC2SConverter;
import com.bai.account.exception.InvalidParameterException;
import com.bai.account.model.service.Record;
import com.bai.account.service.RecordService;
import com.bai.account.service.UserInfoService;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/record")
@Slf4j
public class RecordController {
    private RecordService recordService;
    private RecordC2SConverter c2SConverter;
    private UserInfoService userService;

    /**
     * 构造方法用于注入.
     * @param recordService recordService
     * @param c2SConverter c2SConverter
     * @param userService userservice
     */
    @Autowired
    public RecordController(RecordService recordService, RecordC2SConverter c2SConverter,
                            UserInfoService userService) {
        this.recordService = recordService;
        this.c2SConverter = c2SConverter;
        this.userService = userService;
    }

    /**
     * 查找record通过recordId.
     * @param id record Id
     * @return service层的record
     */
    @GetMapping(path = "/{id}",consumes = "application/json",produces = "application/json")
    public Record findRecordById(@PathVariable("id") Long id) {
        if (id <= 0L) {
            throw new InvalidParameterException("The id is not allowed to be null or less than 0");
        }
        val record = recordService.findRecordById(id);
        return c2SConverter.convert(record);
    }

    /**
     * 创建新的record.
     * @param record 从客户端传过来的record类.
     * @return service的record类.
     */
    @PostMapping(consumes = "application/json",produces = "application/json")
    public Record createRecord(@RequestBody Record record) {
        log.debug("record is :{}",record);
        if (record.getUserId() == null || record.getAmount() == null
            || record.getTagList() == null || record.getCategory() == null) {
            throw new InvalidParameterException("invalid record to created");
        }
        val newRecord = c2SConverter.reverse().convert(record);
        val resource = recordService.createRecord(newRecord);
        return c2SConverter.convert(resource);
    }

    /**
     * 更新record.
     * @param recordId 更新的record id
     * @param record 更新的新record
     * @return
     */
    @PutMapping(path = "/{id}",consumes = "application/json",produces = "application/json")
    public Record updateRecord(@PathVariable("id") Long recordId, @RequestBody Record record) {
        if (recordId <= 0L || record == null) {
            throw new InvalidParameterException("The record id must be not empty and positive.");
        }
        if (record.getUserId() == null || record.getUserId() <= 0L) {
            throw new InvalidParameterException("The user id is empty or invalid");
        }
        userService.findUserInfoById(record.getUserId());
        record.setId(recordId);
        val newRecord = c2SConverter.reverse().convert(record);
        val result = recordService.updateRecord(newRecord);
        return c2SConverter.convert(result);
    }
}

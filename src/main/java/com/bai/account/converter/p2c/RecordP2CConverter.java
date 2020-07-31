package com.bai.account.converter.p2c;

import com.bai.account.model.persistence.Record;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Converter;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
@Component
@AllArgsConstructor(onConstructor_ = {@Autowired})
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class RecordP2CConverter extends Converter<Record, com.bai.account.model.common.Record> {

    private static final String DISABLE = "DISABLE";
    private static final String ENABLE = "ENABLE";
    public static final String INCOME = "INCOME";

    private final TagP2CConverter tagP2CConverter;

    @VisibleForTesting
    public RecordP2CConverter() {
        this.tagP2CConverter = new TagP2CConverter();
    }

    @Override
    protected com.bai.account.model.common.Record doForward(@NotNull Record record) {
        val tagList = ImmutableList.copyOf(tagP2CConverter.convertAll(record.getTagList()));

        return com.bai.account.model.common.Record.builder()
                                                          .id(record.getId())
                                                          .userId(record.getUserId())
                                                          .tagList(tagList)
                                                          .note(record.getNote())
                                                          .category(record.getCategory() != 1 ? "OUTCOME" : "INCOME")
                                                          .status(record.getStatus() != 1 ? DISABLE : ENABLE)
                                                          .amount(record.getAmount())
                                                          .build();
    }

    @Override
    protected Record doBackward(@NotNull com.bai.account.model.common.Record record) {

        val recordResult = Record.builder()
                                 .id(record.getId())
                                 .userId(record.getUserId())
                                 .note(record.getNote())
                                 .status(ENABLE.equals(record.getStatus()) ? 1 : 0)
                                 .category(INCOME.equals(record.getCategory()) ? 1 : 0)
                                 .amount(record.getAmount())
                                 .createTime(LocalDateTime.now())
                                 .build();

        if (record.getTagList() != null) {
            val tagList = ImmutableList.copyOf(tagP2CConverter.reverse().convertAll(record.getTagList()));
            recordResult.setTagList(tagList);
        }

        return recordResult;
    }
}

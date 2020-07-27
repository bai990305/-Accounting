package com.bai.account.converter.c2s;

import com.bai.account.model.common.Tag;

import com.google.common.base.Converter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagC2SConverter extends Converter<Tag, com.bai.account.model.service.Tag> {
    @Override
    protected com.bai.account.model.service.Tag doForward(Tag tag) {
        return com.bai.account.model.service.Tag.builder()
                                            .description(tag.getDescription())
                                            .id(tag.getId())
                                            .userId(tag.getUserId())
                                            .status(tag.getStatus())
                                            .build();
    }

    @Override
    protected Tag doBackward(com.bai.account.model.service.Tag tag) {
        return   Tag.builder()
                    .id(tag.getId())
                    .description(tag.getDescription())
                    .userId(tag.getUserId())
                    .status(tag.getStatus())
                    .build();
    }
}

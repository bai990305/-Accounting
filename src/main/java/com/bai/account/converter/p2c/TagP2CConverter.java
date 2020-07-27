package com.bai.account.converter.p2c;

import com.bai.account.model.persistence.Tag;

import com.google.common.base.Converter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagP2CConverter extends Converter<Tag, com.bai.account.model.common.Tag> {
    private static final String ENABLE = "ENABLE";
    private static final String DISABLE = "DISABLE";
    @Override
    protected com.bai.account.model.common.Tag doForward(Tag tag) {
        return com.bai.account.model.common.Tag.builder()
            .id(tag.getId())
            .description(tag.getDescription())
            .status(tag.getStatus() == 1 ? ENABLE : DISABLE)
            .userId(tag.getUserId())
            .build();
    }

    @Override
    protected Tag doBackward(com.bai.account.model.common.Tag tag) {
        val tagInDb = Tag.builder()
            .id(tag.getId())
            .description(tag.getDescription())
            .userId(tag.getUserId())
            .build();

        if (tag.getStatus() != null) {
            tagInDb.setStatus(ENABLE.equals(tag.getStatus()) ? 1 : 0);
        }

        return tagInDb;
    }
}

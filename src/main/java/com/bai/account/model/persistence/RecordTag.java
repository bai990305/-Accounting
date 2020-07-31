package com.bai.account.model.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordTag {
    private Long id;
    private Long recordId;
    private Long tagId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notice {
    private Integer noticeId;
    private String title;
    private String content;
    private User sender;
    private LocalDateTime createTime;
}

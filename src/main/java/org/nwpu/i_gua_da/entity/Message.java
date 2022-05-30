package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private Integer MessageId;
    private String content;
    private User userId;
    private LocalDateTime messageTime;
}

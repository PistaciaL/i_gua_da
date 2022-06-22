package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private Integer messageId;
    private String content;
    private Integer type;
}

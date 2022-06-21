package org.nwpu.i_gua_da.entity;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class Reserve {
    private Integer reserveId;
    private User user;
    private Schedule schedule;
    private LocalDateTime reserveTime;
    private Integer status;
}

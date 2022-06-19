package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class Schedule implements Serializable {

    private Integer scheduleId;
    private Station startStation;
    private Station endStation;
    /**
     * 发车时间
     */
    private LocalDateTime departureDateTime;
    /**
     * 总座位
     */
    private Integer totalSeat;
    /**
     * 剩余的座位
     */
    private Integer lastSeat;
    /**
     * status: 校车排班状态<br/>
     * 1: 正常<br/>
     * 2: 取消
     */
    private Integer status;
}

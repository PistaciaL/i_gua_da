package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Schedule implements Serializable {
    private Integer scheduleId;
    private Station startStation;
    private Station endStation;
    private LocalDateTime departureTime;
    private Integer totalSeat;
    private Integer lastSeat;
    /**
     * status: 校车排班状态<br/>
     * 1: 正常<br/>
     * 2: 取消
     */
    private Integer status;
}

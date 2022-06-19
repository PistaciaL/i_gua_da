package org.nwpu.i_gua_da.fastjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveData {
    private int reserveId;
    private int status;
    private String startCampus;
    private String startStation;
    private String endCampus;
    private String endStation;
    private String departureDatetime;
}

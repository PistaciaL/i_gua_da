package org.nwpu.i_gua_da.entity;

import lombok.Data;

@Data
public class Station {
    private Integer stationId;
    private String stationName;
    private String stationTelephoneNumb;
    private Integer status;
}

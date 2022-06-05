package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Station implements Serializable {
    private Integer stationId;
    private String stationName;
    private String stationTelephoneNumb;
    private Integer status;
}

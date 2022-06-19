package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Station implements Serializable{
	private Integer stationId;
    private String stationName;
    private Integer status;
	//经度
    private double longitude;
    //纬度
    private double latitude;
    private String campus;
}

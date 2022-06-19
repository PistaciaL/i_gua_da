package org.nwpu.i_gua_da.fastjson;

import lombok.Data;

@Data
public class ScheduleData {
	private int scheduleId;
	private String startCampus;
	private String startStation;
	private String endCampus;
	private String endStation;
	private String departureDatetime;
}

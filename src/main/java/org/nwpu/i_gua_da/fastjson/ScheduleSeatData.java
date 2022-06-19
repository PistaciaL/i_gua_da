package org.nwpu.i_gua_da.fastjson;

import lombok.Data;

@Data
public class ScheduleSeatData {
	private int scheduleId;
	private String startStation;
	private String endStation;
	private String departureDatetime;
	private int lastSeat;
}

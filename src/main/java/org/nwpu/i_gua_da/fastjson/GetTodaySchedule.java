package org.nwpu.i_gua_da.fastjson;

import java.util.List;

import lombok.Data;

@Data
public class GetTodaySchedule {
	private int status;
	private int page;
	private int totalPageNumb;
	private List<ScheduleData> data;
	
}

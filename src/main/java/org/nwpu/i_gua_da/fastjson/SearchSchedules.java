package org.nwpu.i_gua_da.fastjson;

import java.util.List;

import lombok.Data;

@Data
public class SearchSchedules {
	
	private int status;
	private int page;
	private int totalPageNumb;
	private List<AdminScheduleData> data;
}

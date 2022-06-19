package org.nwpu.i_gua_da.fastjson;

import lombok.Data;

import java.util.List;

@Data
public class HistoryReserves {
    private int status;
    private int page;
    private int totalPageNumb;
    private List<ReserveData> data;
}

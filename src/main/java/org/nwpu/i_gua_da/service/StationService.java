package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.Station;

public interface StationService {

    /**
     * 根据station的名字查找station(后期可以添加redis)
     * @param stationName
     * @return
     */
    Station selectStationByStationName(String stationName);

    /**
     * 根据station的id查找station
     * @param stationId
     * @return
     */
    Station selectStationByStationId(Integer stationId);

    /**
     * 增加一个车站
     * @param station
     * @return
     */
    boolean addStation(Station station);

    /**
     * 通过id删除车站
     * @param stationId
     * @return
     */
    boolean removeStation(Integer stationId);
}

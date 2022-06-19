package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.Station;

@Mapper
public interface StationMapper {

    Station selectStationByStationName(@Param("stationName") String stationName);

    Station selectStationByStationId(@Param("stationId") Integer stationId);

    int addStation(Station station);

    int addNoticeDefaultStatus(Station station);

    int removeStationById(@Param("stationId") Integer stationId, @Param("isDeleteStatus") Integer isDeleteStatus);
}

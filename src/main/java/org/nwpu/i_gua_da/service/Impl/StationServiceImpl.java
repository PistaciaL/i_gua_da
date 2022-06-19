package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.Station;
import org.nwpu.i_gua_da.mapper.StationMapper;
import org.nwpu.i_gua_da.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${constants.station.length.stationName}")
    private int StationNameLength;
    @Value("${constants.station.length.campus}")
    private int StationCampusLength;
    @Value("${constants.station.status.isDelete}")
    private int stationIsDeleteStatus;
    @Value("redisKey.station")
    private String stationRedisKey;


    @Override
    public Station selectStationByStationName(String stationName) {
        if(stationName == null)
            throw new NullPointerException();
        if("".equals(stationName))
            throw new IllegalArgumentException();
        Station station = stationMapper.selectStationByStationName(stationName);
        if(station == null)
            return null;
        if(redisTemplate.opsForValue().get(stationRedisKey+":"+station.getStationId()) == null)
            redisTemplate.opsForValue().set(stationRedisKey+":"+station.getStationId(), station, 10, TimeUnit.MINUTES);
        if (station.getStatus() == stationIsDeleteStatus)
            throw new NullPointerException();
        return station;
    }

    @Override
    public Station selectStationByStationId(Integer stationId) {
        if(stationId == null)
            throw new NullPointerException();
        if(stationId < 0)
            throw new NullPointerException();
        Station station = stationMapper.selectStationByStationId(stationId);
        if (station.getStatus() == stationIsDeleteStatus)
            throw new NullPointerException();
        return station;
    }

    @Override
    public boolean addStation(Station station) {
        if(station == null || station.getStationName() == null || station.getCampus() == null)
            throw new NullPointerException();
        if(station.getStationName().length() == 0 || station.getStationName().length() > StationNameLength ||
                station.getCampus().length() == 0 || station.getCampus().length() > StationCampusLength ||
                station.getLongitude() < -180.0 || station.getLongitude() > 180.0 ||
                station.getLatitude() < -90.0 || station.getLatitude() < 90.0)
            throw new IllegalArgumentException();

        int i = 0;
        if(station.getStatus() == null)
            i = stationMapper.addNoticeDefaultStatus(station);
        else
            i = stationMapper.addStation(station);
        return i == 1;
    }

    @Override
    public boolean removeStation(Integer stationId) {
        if(stationId == null)
            throw new NullPointerException();
        if(stationId < 0)
            throw new IllegalArgumentException();
        int i = stationMapper.removeStationById(stationId, stationIsDeleteStatus);
        return i == 1;
    }
}

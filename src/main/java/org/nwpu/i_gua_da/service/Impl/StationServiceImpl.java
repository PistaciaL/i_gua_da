package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.Station;
import org.nwpu.i_gua_da.mapper.StationMapper;
import org.nwpu.i_gua_da.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private RedisTemplate redisTemplate;

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
        return station;
    }

    @Override
    public Station selectStationByStationId(Integer stationId) {
        if(stationId == null)
            throw new NullPointerException();
        if(stationId < 0)
            throw new NullPointerException();
        Station station = stationMapper.selectStationByStationId(stationId);
        return station;
    }
}

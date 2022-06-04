package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduleMapper {

    List<Schedule> getSchedulesByScheduleId(Integer userId);

    List<Schedule> listAllSchedules();

    List<Schedule> listScheduleBetweenTimes(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}

package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduleMapper {

    Schedule getSchedulesByScheduleId(Integer scheduleId);

    List<Schedule> listAllSchedules();

    List<Schedule> listScheduleBetweenTimes(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("status") Integer notDeleteStatus);

    Integer addSchedule(Schedule schedule);

    Integer updateLastSeatByScheduleId(Schedule schedule);

    Integer setStatusByScheduleId(@Param("scheduleId") Integer scheduleId, @Param("status")Integer status);
}

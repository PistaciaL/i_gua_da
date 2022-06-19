package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduleMapper {

	/**
	 * 通过班车Id查找班车
	 * @param scheduleId
	 * @return 符合条件的班车
	 */
    Schedule getSchedulesByScheduleId(Integer scheduleId);

    /**
     * 列出所有班车
     * @return 所有班车列表
     */
    List<Schedule> listAllSchedules();

    /**
     * 通过出发地，目的地和起止时间筛选符合条件的班车
     * @param startTime
     * @param endTime
     * @param notDeleteStatus
     * @param startStation
     * @param endStation
     * @return 符合条件的班车列表
     */
    List<Schedule> listScheduleByStationAndTime(@Param("startTime") LocalDateTime startTime, 
    		@Param("endTime") LocalDateTime endTime, @Param("status") Integer notDeleteStatus,
    		@Param("startStation") String startStation, @Param("endStation") String endStation);
    
    /**
     * 通过起止时间筛选符合条件的班车
     * @param startTime
     * @param endTime
     * @param notDeleteStatus
     * @return 符合条件的班车列表
     */
    List<Schedule> listScheduleBetweenTimes(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("status") Integer notDeleteStatus);

    /**
     * 管理员添加班车
     * @param schedule
     * @return 返回成功操作的记录数目
     */
    Integer addSchedule(Schedule schedule);

    /**
     * 通过班车Id更新班车的剩余座位
     * @param schedule
     * @return 返回1代表更新成功
     */
    Integer updateLastSeatByScheduleId(Schedule schedule);
  
    /**
     * 通过班车Id设置班车状态
     * @param scheduleId
     * @param status
     * @return 返回1代表正常，2代表班车取消
     */
    Integer setStatusByScheduleId(@Param("scheduleId") Integer scheduleId, @Param("status")Integer status);
}

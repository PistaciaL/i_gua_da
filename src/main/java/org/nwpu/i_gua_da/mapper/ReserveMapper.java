package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.Reserve;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReserveMapper {

    int insertReserve(Reserve reserve);

    /**
     * 不进行联表查询
     * @param userId
     * @return
     */
    List<Reserve> listOnlyReservesByUserId(Integer userId);

    List<Reserve> listReservesByUserIdBetweenTimes(@Param("userId") Integer userId, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

    int setStatusByUserIdAndScheduleId(@Param("userId") Integer userId, @Param("scheduleId") Integer scheduleId, @Param("status")Integer status);

    Reserve selectReserveByReserveId(Integer reserveId);

    int setStatusByReserveId(@Param("reserveId") Integer reserveId, @Param("status")Integer status);

    Integer verifyReserveByScheduleIdAndUserId(@Param("userId") Integer userId, @Param("scheduleId") Integer scheduleId);
}

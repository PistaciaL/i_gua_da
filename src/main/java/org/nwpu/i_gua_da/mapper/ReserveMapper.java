package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.Reserve;

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

    List<Reserve> listReservesByUserId(Integer userId);


    int deleteReserveByUserIdAndScheduleId(@Param("userId") Integer userId, @Param("scheduleId") Integer scheduleId);

}

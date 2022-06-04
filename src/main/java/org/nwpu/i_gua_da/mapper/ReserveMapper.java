package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nwpu.i_gua_da.entity.Reserve;

import java.util.List;

@Mapper
public interface ReserveMapper {

    int insertReserve(Reserve reserve);

}

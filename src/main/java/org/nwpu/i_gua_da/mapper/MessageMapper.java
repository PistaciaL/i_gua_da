package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.Message;

import java.util.List;

@Mapper
public interface MessageMapper {

    int addMessage(Message message);
    int addMessageDefaultType(Message message);

    List<Message> listMessageByType(@Param("type") Integer type);

}
package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.Message;

import java.util.List;

@Mapper
public interface MessageMapper {

    /**
     * 添加留言
     * @param message 要添加的留言
     * @return
     */
    int addMessage(Message message);

    /**
     * 添加默认类型的留言
     * @param message 要添加的留言
     * @return
     */
    int addMessageDefaultType(Message message);

    /**
     * 获取特定类型的留言列表
     * @param type 留言类型
     * @return
     */
    List<Message> listMessageByType(@Param("type") Integer type);

}
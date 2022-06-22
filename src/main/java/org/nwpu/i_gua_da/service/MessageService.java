package org.nwpu.i_gua_da.service;

import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Message;

import java.util.List;

public interface MessageService {

    /**
     * 用户发布留言
     * @param message
     * @return
     */
    public boolean addMessage(Message message);

    /**
     * 管理员查看留言
     * @param type
     * @return
     */
    public List<Message> listMessageByType(Integer type);

}

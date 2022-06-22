package org.nwpu.i_gua_da.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Message;
import org.nwpu.i_gua_da.mapper.MessageMapper;
import org.nwpu.i_gua_da.service.AdminService;
import org.nwpu.i_gua_da.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Value("${constants.message.length.content}")
    private int messageContentMaxLength;
    @Value("${constants.message.type.default}")
    private int messageTypeDefault;

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private AdminService adminService;

    @Override
    @Transactional
    public boolean addMessage(Message message) {
        if(message == null || message.getContent() == null || message.getType() == null)
            throw new NullPointerException();
        if(message.getContent().length() == 0 || message.getContent().length() > messageContentMaxLength)
            throw new IllegalArgumentException();
        int i = 0;
        if(message.getType() == null){
            message.setType(messageTypeDefault);
            i = messageMapper.addMessageDefaultType(message);
        }
        else
            i = messageMapper.addMessage(message);
        return i == 1;
    }

    @Override
    public List<Message> listMessageByType(Integer type) {
        if(type == null) throw new NullPointerException();
        List<Message> messages = messageMapper.listMessageByType(type);
        if(messages.size() == 0) return null;
        return messages;
    }
}
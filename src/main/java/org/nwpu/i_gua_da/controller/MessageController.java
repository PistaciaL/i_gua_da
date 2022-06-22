package org.nwpu.i_gua_da.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Message;
import org.nwpu.i_gua_da.entity.Notice;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.MessageService;
import org.nwpu.i_gua_da.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @RequestMapping("/addMessage")
    public String addMessage(@RequestParam("content") String content,
                            @RequestParam("type") Integer type,
                             @RequestParam("code")String code){

        Message newMessage = new Message();
        if(content == null)
            return "{\"status\":420}";
        newMessage.setContent(content);

        if(type == null){
            newMessage.setType(1);
        }
        newMessage.setType(type);
        try {

            if(messageService.addMessage(newMessage)) {
                return "{\"status\":200}";
            }else {
                return "{\"status\":420}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":420}";
        }
    }

}

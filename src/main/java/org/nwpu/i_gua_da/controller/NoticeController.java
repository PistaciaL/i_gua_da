package org.nwpu.i_gua_da.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Notice;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.service.NoticeService;
import org.nwpu.i_gua_da.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;

    DateTimeFormatter dfOut = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.CHINA);

    /**
     * 获取通知
     * @param page 页码
     * @param pageSize 一页大小
     * @return 获取时间上最新的且未被删除的五条通知
     * 状态码，status=200表示查询成功，status=420表示查询失败，
     */
    @RequestMapping("/getNotices")
    public String GetNotice(@RequestParam("page")int page,
                            @RequestParam("pageSize")int pageSize){
        PageInfo<Notice> pageInfo = null;
        try {
            pageInfo = noticeService.getNoticeList(page,pageSize);
        } catch (Exception e) {
            return "{\"status\":420}";
        }
        List<Map<String,Object>> data = new ArrayList<>();
        if (pageInfo == null){
            return "{\"status\":420}";
        }
        Map<String,Object> result = new HashMap<>();
        result.put("status",200);
        result.put("page",page);
        result.put("totalPageNumb",pageInfo.getPages());
        List<Notice> notices = pageInfo.getList();
        for (Notice notice : notices) {
            Map<String, Object> map = new HashMap<>();
            map.put("noticeId",notice.getNoticeId());
            map.put("title",notice.getTitle());
            map.put("content",notice.getContent());
            data.add(map);
        }
        result.put("data",data);
        return JSON.toJSONString(result);
    }

}

package org.nwpu.i_gua_da.controller;

import org.nwpu.i_gua_da.api.CommonResult;
import org.nwpu.i_gua_da.entity.Notice;
import org.nwpu.i_gua_da.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 获得当日通知
 */
@RestController
public class GetNoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 获取通知
     * @return 获取时间上最新的且未被删除的五条通知
     */
    @RequestMapping("/getNoitces")
    public CommonResult GetNotice(){
        List<Notice> notices;
        notices = noticeService.getNoticeList(1,5);
        if (notices == null){
            return CommonResult.failed();
        }
        return CommonResult.success(notices);
    }
}

package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.Notice;

import java.util.List;

public interface NoticeService {

    /**
     * 获取最新的公告
     * @return 返回公告列表
     */
    public List<Notice> getNoticeList(Integer PageNum, Integer PageSize);

    /**
     * 发布公告
     * @param Notice 发布的公告
     * @return true发布成功
     */
    public boolean addNotice(Notice Notice);

    /**
     * 管理员删除公告
     * @param NoticeId 公告id
     * @return true 删除公告成功
     */
    public boolean removeNotice(Integer NoticeId);

    /**
     * 根据公告标题搜索公告
     * @param NoticeTitle 公告标题
     * @return 搜索出来的公告
     */
    public List<Notice> searchNotice(String NoticeTitle);

    /**
     * 根据公告id搜索公告
     * @param NoticeId 公告id
     * @return 搜索出来的公告
     */
    public Notice searchNotice(Integer NoticeId);
}

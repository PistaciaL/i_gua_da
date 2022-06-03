package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.Notice;
import org.nwpu.i_gua_da.mapper.NoticeMapper;
import org.nwpu.i_gua_da.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Value("${constants.notice.length.title}")
    private int noticeTitleMaxLength;
    @Value("${constants.notice.length.content}")
    private int noticeContentMaxLength;

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    @Transactional
    public boolean addNotice(Notice notice) {
        if(notice == null || notice.getTitle() == null || notice.getContent() == null ||
                notice.getSender() == null || notice.getSender().getUserId() == null)
            throw new NullPointerException();
        if(notice.getTitle().length() == 0 || notice.getTitle().length() > noticeTitleMaxLength ||
                notice.getContent().length() == 0 || notice.getContent().length() > noticeContentMaxLength ||
                notice.getSender().getUserId() < 0)
            throw new IllegalArgumentException();
        if(notice.getCreateTime() == null)
            notice.setCreateTime(LocalDateTime.now());
        int i = noticeMapper.addNotice(notice);
        return i == 1;
    }

    @Override
    public List<Notice> getNoticeList(Integer pageNum, Integer pageSize) {
        if(pageNum == null || pageSize == null)
            throw new NullPointerException();
        if(pageNum < 0 || pageSize < 0)
            throw new IllegalArgumentException();
        PageHelper
    }

    @Override
    public boolean removeNotice(Integer noticeId) {
        if(noticeId == null)
            throw new NullPointerException();
        if(noticeId < 0)
            throw new IllegalArgumentException();
        int i = noticeMapper.removeNoticeById(noticeId);
        return i == 1;
    }

    @Override
    public List<Notice> searchNotice(String noticeTitle) {
        if(noticeTitle == null)
            throw new NullPointerException();
        if(noticeTitle.length() == 0 || noticeTitle.length() > noticeTitleMaxLength)
            throw new IllegalArgumentException();
        return noticeMapper.listNoticeByNoticeTitle(noticeTitle);
    }

    @Override
    public Notice searchNotice(Integer noticeId) {
        if(noticeId == null)
            throw new NullPointerException();
        if(noticeId < 0)
            throw new IllegalArgumentException();
        return noticeMapper.searchNoticeById(noticeId);
    }
}
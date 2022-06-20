package org.nwpu.i_gua_da.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Notice;
import org.nwpu.i_gua_da.entity.User;
import org.nwpu.i_gua_da.mapper.NoticeMapper;
import org.nwpu.i_gua_da.service.AdminService;
import org.nwpu.i_gua_da.service.NoticeService;
import org.nwpu.i_gua_da.service.UserService;
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
    @Value("${constants.user.status.notDelete}")
    private int noticeNotDeleteStatus;
    @Value("${constants.user.status.isDelete}")
    private int noticeIsDeleteStatus;

    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private AdminService adminService;

    @Override
    @Transactional
    public boolean addNotice(Notice notice) {
        if(notice == null || notice.getTitle() == null || notice.getContent() == null)
            throw new NullPointerException();
        if(notice.getTitle().length() == 0 || notice.getTitle().length() > noticeTitleMaxLength ||
                notice.getContent().length() == 0 || notice.getContent().length() > noticeContentMaxLength)
            throw new IllegalArgumentException();
        //User user = adminService.searchUser(notice.getSender().getUserId());
        //if(user == null)
        //    throw new RuntimeException("发送公告的用户不存在");
        if(notice.getCreateTime() == null)
            notice.setCreateTime(LocalDateTime.now());
        int i = 0;
        if(notice.getStatus() == null)
            i = noticeMapper.addNoticeDefaultStatus(notice);
        else
            i = noticeMapper.addNotice(notice);
        return i == 1;
    }

    @Override
    public PageInfo<Notice> getNoticeList(Integer pageNum, Integer pageSize) {
        if(pageNum == null || pageSize == null)
            throw new NullPointerException();
        if(pageNum < 1 || pageSize <= 0)
            throw new IllegalArgumentException();
        PageHelper.startPage(pageNum, pageSize);
        //PageHelper和调用noticeMapper之间不能有其他语句
        List<Notice> notices = noticeMapper.listNotices(noticeNotDeleteStatus);
        PageInfo<Notice> pageInfo = new PageInfo<>(notices);
        return pageInfo;
    }

    @Override
    public boolean removeNotice(Integer noticeId) {
        if(noticeId == null)
            throw new NullPointerException();
        if(noticeId < 0)
            throw new IllegalArgumentException();
        int i = noticeMapper.removeNoticeById(noticeId, noticeIsDeleteStatus);
        return i == 1;
    }

    @Override
    public PageInfo<Notice> searchNotice(String noticeTitle, Integer pageNum, Integer pageSize) {
        if(noticeTitle == null || pageNum == null || pageSize == null)
            throw new NullPointerException();
        if(noticeTitle.length() == 0 || noticeTitle.length() > noticeTitleMaxLength || pageNum < 1 || pageSize < 1)
            throw new IllegalArgumentException();
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> notices = noticeMapper.listNoticeByNoticeTitle(noticeTitle, noticeNotDeleteStatus);
        if (notices.size() == 0) return null;
        PageInfo<Notice> pageInfo = new PageInfo<>(notices);
        return pageInfo;
    }

    @Override
    public Notice searchNotice(Integer noticeId) {
        if(noticeId == null)
            throw new NullPointerException();
        if(noticeId < 0)
            throw new IllegalArgumentException();
        return noticeMapper.searchNoticeById(noticeId);
    }

    @Override
    public PageInfo<Notice> listNoticeByNoticeTitleLike(String noticeTitle, Integer pageNum, Integer pageSize) {
        if(noticeTitle == null || pageNum == null || pageSize == null)
            throw new NullPointerException();
        if(noticeTitle.length() == 0 || noticeTitle.length() > noticeTitleMaxLength || pageNum < 1 || pageSize < 1)
            throw new IllegalArgumentException();
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> notices = noticeMapper.listNoticeByNoticeTitleLike(noticeTitle, noticeNotDeleteStatus);
        if (notices == null) return null;
        PageInfo<Notice> pageInfo = new PageInfo<>(notices);
        return pageInfo;
    }
}
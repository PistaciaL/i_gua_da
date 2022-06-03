package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nwpu.i_gua_da.entity.Notice;

import java.util.List;

@Mapper
public interface NoticeMapper {

    int addNotice(Notice notice);

    int removeNoticeById(Integer noticeId);

    List<Notice> listNoticeByNoticeTitle(String noticeTitle);

    Notice searchNoticeById(Integer noticeId);
}
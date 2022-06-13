package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nwpu.i_gua_da.entity.Notice;

import java.util.List;

@Mapper
public interface NoticeMapper {

    int addNotice(Notice notice);
    int addNoticeDefaultStatus(Notice notice);

    int removeNoticeById(@Param("noticeId") Integer noticeId, @Param("isDeleteStatus") Integer isDeleteStatus);

    List<Notice> listNoticeByNoticeTitle(@Param("noticeTitle") String noticeTitle, @Param("notDeleteStatus") Integer notDeleteStatus);

    Notice searchNoticeById(Integer noticeId);

    List<Notice> listNotices(Integer notDeleteStatus);

    List<Notice> listNoticeByNoticeTitleLike(@Param("noticeTitle") String noticeTitle, @Param("notDeleteStatus") Integer notDeleteStatus);
}
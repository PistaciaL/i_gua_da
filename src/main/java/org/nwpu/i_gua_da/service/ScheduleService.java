package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {

    /**
     * 获取所有校车班车
     * @param PageNum 页码数
     * @param PageSize 一页大小
     * @return 所有的班次信息
     */
    public List<Schedule> getSchedule(Integer PageNum, Integer PageSize);

    /**
     * 查找时间区域内的班次信息
     * @param StartTime 查找的开始时间
     * @param EndTime 查找的结束时间
     * @param PageNum 页码数
     * @param PageSize 一页大小
     * @return 返回在查找时间内的所有班次
     */
    public List<Schedule> findSchedule(LocalDateTime StartTime, LocalDateTime EndTime, Integer PageNum, Integer PageSize);

    /**
     * 通过班次id查找班次
     * @param scheduledId 班次id
     * @return 返回查找的班次
     */
    public Schedule getScheduleId(Integer scheduledId);

}

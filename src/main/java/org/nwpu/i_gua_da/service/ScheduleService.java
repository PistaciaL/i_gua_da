package org.nwpu.i_gua_da.service;

import java.time.LocalDateTime;
import java.util.List;

import com.github.pagehelper.PageInfo;
import org.nwpu.i_gua_da.entity.Schedule;

public interface ScheduleService {
	
	/**
     * 获取所有校车班车
     * (所有查询失败的结果都返回null)
     * @param PageNum 页码数
     * @param PageSize 一页大小
     * @return 所有的班次信息
     */
    public PageInfo<Schedule> getSchedule(Integer PageNum, Integer PageSize);

    /**
     * 查找时间区域内的班次信息
     * (所有查询失败的结果都返回null)
     * @param StartTime 查找的开始时间
     * @param EndTime 查找的结束时间
     * @param PageNum 页码数
     * @param PageSize 一页大小
     * startTime="",endTime="2022/5/26"表示查询该用户发车时间小于2022/5/26 24:00的所有预约
     * startTime="2022/5/26",endTime=""表示查询该用户发车时间大于2022/5/26 00:00的所有预约
     * startTime="2022/5/26",endTime="2022/5/26"表示查询该用户发车时间在2022/5/26 00:00到2022/5/26 24:00之间的所有预约
     * 我把所有的字符串转化成LocalDateTime的格式传递参数
     * @return 返回在查找时间内的所有班次
     */
    public PageInfo<Schedule> findSchedule(LocalDateTime StartTime, LocalDateTime EndTime, Integer PageNum, Integer PageSize);

    /**
     * 查找时间区域内和指定出发地，目的地的班次信息
     * (所有查询失败的结果都返回null)
     * @param StartTime
     * @param EndTime
     * @param startStationName
     * @param endStationName
     * @param PageNum
     * @param PageSize
     * @return 返回符合条件的所有班次
     */
    public PageInfo<Schedule> findScheduleByStationAndTime(LocalDateTime StartTime, LocalDateTime EndTime,
    		String startStationName, String endStationName, Integer PageNum, Integer PageSize);
    
    /**
     * 通过班次id查找班次
     * (所有查询失败的结果都返回null)
     * @param scheduledId 班次id
     * @return 返回查找的班次
     */
    public Schedule getScheduleId(Integer scheduledId);

    /**
     * 添加一个新班次
     * @param schedule 添加的新班次
     * @return true添加成功 / fasle添加失败
     */
    public boolean addSchcedule(Schedule schedule);

    /**
     * 根据id删除一个班次
     * @param scheduledId 要删除的班次的id
     * @return true删除成功 / false删除失败
     */
    public boolean removeSchcedule(Integer scheduledId);
     
}

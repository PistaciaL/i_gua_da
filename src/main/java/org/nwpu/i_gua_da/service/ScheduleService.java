package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.Schedule;

import java.util.List;

public interface ScheduleService {

    /**
     * 获取所有校车班车
     * @return 所有的班次信息
     */
    public List<Schedule> getSchedule();

    /**
     * 查找时间区域内的班次信息
     * @param StartTime 查找的开始时间
     * @param EndTime 查找的结束时间
     * @return 返回在查找时间内的所有班次，若时间错误(开始时间晚于结束时间)，返回"$error"
     */
    public List<Schedule> findSchedule(String StartTime, String EndTime);

    /**
     * 预约校车班次
     * @param ScheduleId 预约班次的id
     * @return true预约成功，false剩余座位不够
     */
    public boolean bookingSchedule(String ScheduleId);

    /**
     * 查看用户自己的预约班次
     * @param username 根据用户名字查找用户班次
     * @return 返回用户自己的预约班次
     */
    public List<Schedule> getUserSchedule(String username);

    /**
     * 用户取消班次预约
     * @param scheduleId 班次id号
     * @return true 取消预约成功
     */
    public boolean removeSchedule(Integer scheduleId);
}

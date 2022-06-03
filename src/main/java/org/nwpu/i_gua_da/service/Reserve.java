package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.User;

import java.util.List;

public interface Reserve {

    /**
     * 预约校车班次
     * @param reserve 预约班次的id
     * @return true预约成功，false剩余座位不够
     */
    public boolean bookingSchedule(Reserve reserve);

    /**
     * 查看用户自己的预约班次
     * @param user 根据用户名字查找用户班次
     * @return 返回用户自己的预约班次
     */
    public List<Schedule> getUserSchedule(User user);

    /**
     * 用户取消班次预约
     * @param scheduleId 班次id号
     * @param user 用户
     * @return true 取消预约成功
     */
    public boolean removeReserve(User user, Integer scheduleId);
}

package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.Schedule;
import org.nwpu.i_gua_da.entity.User;

import java.util.List;

public interface Reserve {

    /**
     * 用户预约校车
     * @param reserve 预约班次对象，（reserveId需要自增实现，传递的对象里的reserveId为null）
     * @return true预约成功，false剩余座位不够
     */
    public boolean bookingReserve(Reserve reserve);

    /**
     * 用户查看自己的预约
     * @param userId 根据用户id查找用户班次
     * @param PageSize 页码数
     * @param PageNum 一页大小
     * @return 返回用户自己的预约列表
     */
    public List<Reserve> getUserReserve(Integer userId, Integer PageNum, Integer PageSize);

    /**
     * 用户取消预约，通过用户id和班次的id删除用户的预约(删除reserve)
     * @param UserId 用户id号
     * @param ScheduleId 班次的id
     * @return true 取消预约成功
     */
    public boolean removeReserve(Integer UserId, Integer ScheduleId);
}

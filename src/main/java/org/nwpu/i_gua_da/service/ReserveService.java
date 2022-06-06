package org.nwpu.i_gua_da.service;

import org.nwpu.i_gua_da.entity.Reserve;

import java.time.LocalDateTime;
import java.util.List;

public interface ReserveService {
    /**
     * 用户预约校车
     * @param reserve 预约班次对象，（reserveId需要自增实现，传递的对象里的reserveId为null）
     * @return true预约成功，false剩余座位不够
     */
    public boolean bookingReserve(Reserve reserve);

    /**
     * 用户查看自己的预约
     * (所有查询失败的结果都返回null)
     * @param userId 根据用户id查找用户班次
     * @param PageSize 页码数
     * @param PageNum 一页大小
     * @param StartTime 开始时间
     * @param EndTime 结束时间
     * 例如：
     * startTime="",endTime=""表示查询该用户所有预约
     * startTime="",endTime="2022/5/26"表示查询该用户发车时间小于2022/5/26 24:00的所有预约
     * startTime="2022/5/26",endTime=""表示查询该用户发车时间大于2022/5/26 00:00的所有预约
     * startTime="2022/5/26",endTime="2022/5/26"表示查询该用户发车时间在2022/5/26 00:00到2022/5/26 24:00之间的所有预约
     * 我把所有的字符串转化成LocalDateTime的格式传递参数
     * @return 返回用户自己的预约列表
     */
    public List<Reserve> getUserReserve(Integer userId, LocalDateTime StartTime, LocalDateTime EndTime, Integer PageNum, Integer PageSize);

    /**
     * 用户取消预约，通过用户id和班次的id删除用户的预约(删除reserve)
     * @param UserId 用户id号
     * @param ScheduleId 班次的id
     * @return true 取消预约成功
     */
    public boolean removeReserve(Integer UserId, Integer ScheduleId);
}

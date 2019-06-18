package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * Created by zhuhp on 2016/12/24.
 */
public interface SuccessKillDao {
    /**
     *插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return 如果返行为>1 ，表示插入成功
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询 successkilled 并带上秒杀实体对象
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);



}

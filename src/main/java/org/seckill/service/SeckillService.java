package org.seckill.service;

import org.seckill.model.Exposer;
import org.seckill.model.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 三个方面：方法定义粒度，参数，返回类型（return 类型/异常）
 * Created by zhuhp on 2016/12/25.
 */

public interface SeckillService {
    /**
     * 查询所有秒杀接口
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀接口
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开始时输出秒杀接口地址和时间，
     * 否则输出系统时间和秒杀时间
     * @return 输入的是一个数据传输实体
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀业务 <br/>
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return 返回执行秒杀结果对象
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillCloseException,SeckillException,RepeatKillException;
}

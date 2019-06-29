package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKillDao;
import org.seckill.cache.RedisDao;
import org.seckill.model.Exposer;
import org.seckill.model.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuhp on 2016/12/25.
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKillDao successKillDao;

//    @Autowired
//    private RedisDao redisDao;
    //盐值
    private final String salt = "fdkr48375030fidasfsi!@$#@$^&FADFVfsjafwqiSTR^";

    @Override
    public List<Seckill> getSeckillList() {
        try {
            return seckillDao.queryAll(0, 4);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
        }
        return Collections.emptyList();

    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    /**
     * 暴露出seckillBean
     *
     * @param seckillId
     * @return
     */
    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        // 优化点：超时基础上维护一致性
        // redis 缓存

        //TODO: 判断redis 是否开启 jedis.ping
//        System.out.println("jedis status: " + new Jedis().ping());
//        String s = new Jedis().ping();
//        if ("".equals(s) || !"PONG".equals(s)) {
//            logger.error("redis 未启动");
//            return null;
//        }

        Seckill seckill = seckillDao.queryById(seckillId);
//        Seckill seckill = redisDao.getSeckill(seckillId);

//        if (seckill == null) {
//            // access db
//            seckill = seckillDao.queryById(seckillId);
//            if (seckill == null) {
//                return new Exposer(false, seckillId);
//            } else {
//                // redis中添加有效访问时间
//                redisDao.putSeckill(seckill);
//            }
//        }

        if (seckill == null) {
            return new Exposer(false, seckillId);
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillCloseException, SeckillException, RepeatKillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存 ，记录购买行为
        Date nowTime = new Date();

        // TODO: 使用消息队列 + 事务处理
        try {
            // 减库存操作
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);

            if (updateCount <= 0) {
                throw new SeckillCloseException("seckill  is closed");

            } else {
                // 更新秒杀成功结果数据表
                int insertCount = successKillDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    //重复插入数据
                    throw new RepeatKillException("repeate seckill");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKillDao.queryByIdWithSeckill(seckillId, userPhone);
                    logger.info(successKilled.toString());
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译异常转化为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }

    }

    /**
     * 加盐操作
     *
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}

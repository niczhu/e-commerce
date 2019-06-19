package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuhp on 2016/12/24.
 */

/**
 * 配置spring 与 unit 整合。junit启动时加载 springIoc容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring 配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    //注入dao实现类
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void QueryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> list = seckillDao.queryAll(1001,3);
        for (Seckill seckill : list) {
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber() throws Exception {
        Date killtime=new Date();
        int i = seckillDao.reduceNumber(1000,killtime);
            System.out.println(i);

    }



}
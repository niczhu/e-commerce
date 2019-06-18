package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.model.Exposer;
import org.seckill.model.SeckillExecution;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by zhuhp on 2016/12/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-service.xml",
        "classpath:spring/spring-dao.xml"})
public class SeckillServiceTest {
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("list={}",seckillList);
        System.out.println(seckillList);

    }

    @Test
    public void getById() throws Exception {
        Seckill seckill = seckillService.getById(1000L);
        logger.info("seckill={}",seckill);

    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);
//        if(exposer.isExposed()){
//
//            long userphone = 15201443724L;
//            String md5 = exposer.getMd5();
//            SeckillExecution seckillExecution = seckillService.executeSeckill(id,userphone,md5);
//            logger.info("seckillExecution={}",seckillExecution);
//        }else {
//            System.out.println("秒杀未开启");
//        }
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1001;
        String md5="81501a192ed601cdb78429100669a3c5";
        long userphone = 15201443724L;
        SeckillExecution seckillExecution = seckillService.executeSeckill(id,userphone,md5);
        logger.info("seckillExecution={}",seckillExecution);


    }

}
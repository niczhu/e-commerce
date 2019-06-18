package org.seckill.model;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;

/**
 * 封装秒杀执行后的结果
 * Created by zhuhp on 2016/12/25.
 */
public class SeckillExecution {

    private long seckillId;

    //秒杀执行的结果状态
    private int state;

    //秒杀标示
    private String stateInfo;

    //秒杀成功对象
    private SuccessKilled seccusskilled;

    //执行成功要返回信息的构造函数
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum,  SuccessKilled seccusskilled) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.seccusskilled = seccusskilled;
    }
    //构造函数，执行失败要返回的构造函数
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSeccusskilled() {
        return seccusskilled;
    }

    public void setSeccusskilled(SuccessKilled seccusskilled) {
        this.seccusskilled = seccusskilled;
    }
}

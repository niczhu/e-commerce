package org.seckill.enums;

/**
 * 使用枚举表示 状态
 * Created by zhuhp on 2016/12/25.
 */
public enum SeckillStateEnum {

    SUCCESS(1,"成功秒杀"),
    END(2,"秒杀结束"),
    REPEAT_KILL(3,"重复秒杀"),
    INNER_ERROR(4,"系统异常"),
    DATA_REWRITE(5,"数据篡改");

    private int state;

    private String stateInfo;

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStateEnum stateOf(int index){
        for(SeckillStateEnum state : values()){
            if(state.getState() == index){
                return state;
            }
        }
        return null;
    }

}

package org.seckill.exception;

/**
 * 重复秒杀异常
 * Created by zhuhp on 2016/12/25.
 */
public class RepeatKillException extends SeckillCloseException{
    public RepeatKillException(String message) {
        super(message);

    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}

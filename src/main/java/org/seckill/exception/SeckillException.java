package org.seckill.exception;

/**
 * 秒杀相关异常 baseSeckillException
 * Created by zhuhp on 2016/12/25.
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}

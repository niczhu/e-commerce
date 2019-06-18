package org.seckill.exception;

/** 秒杀关闭异常
 * Created by zhuhp on 2016/12/25.
 */

public class SeckillCloseException extends SecurityException {


    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }

}

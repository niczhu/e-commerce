package org.seckill.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * @Author zhuhp
 * @Date 2019/6/29
 */
public class RedisCacheTransfer {
    @Autowired
    public void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
        RedisCache.setJedisConnectionFactory(jedisConnectionFactory);
    }

}

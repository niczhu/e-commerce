package org.seckill.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 优化时使用，可以用redis优化<br/>
 * 把秒杀接口的暴露用redis缓存
 * Created by zhuhp on 2016/12/27.
 */
public class RedisDao {
    
    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    private final String JEDIS_KEY = "seckill";

    public RedisDao(String ip,int port) {
        this.jedisPool = new JedisPool(ip,port);
    }
    //调用protostuff的schema来完成序列化对象
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(long seckillId){
        logger.info("get data from redis");

        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = JEDIS_KEY + seckillId;
                //redis并没有实现序列化操作，
                //get->byte[]-->反序列化-->object(seckill)
                //即把二进制反序列化o为对象！
                //采用自定义序列化工具类 protostuff :pojo
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes !=null){
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    //被反序列化
                    return seckill;
                }
            }finally {
                jedis.close();
            }

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public String putSeckill(Seckill seckill){
        
        logger.info("put data into redis");

        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = JEDIS_KEY + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存 2 min
                int timeout = 60*1;
                String result = jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }finally{
                jedis.close();
            }

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

}

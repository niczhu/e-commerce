package org.seckill.cache;


import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author zhuhp
 * @Date 2019/6/29
 */
public class RedisCache implements Cache {


    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private static JedisConnectionFactory jedisConnectionFactory;

    private final String id;


    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public RedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }

        this.id = id;

        logger.debug("Mybatis-Redis-Cache:id=" + id);
        System.out.println("Mybatis-Redis-Cache:id=" + id);
    }


    /**
     * @return The identifier of this cache
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * @param key   Can be any object but usually it is a {@link CacheKey}
     * @param value The result of a select.
     */
    @Override
    public void putObject(Object key, Object value) {
        System.out.println("===redis put object k="+key);
        logger.info("===redis put object k="+key);
        JedisConnection connection = null;

        try
        {
            connection = jedisConnectionFactory.getConnection();
            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
            connection.set(serializer.serialize(key), serializer.serialize(value));
        }
        catch (JedisConnectionException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null) {
                connection.close();
            }
        }

    }

    /**
     * @param key The key
     * @return The object stored in the cache.
     */
    @Override
    public Object getObject(Object key) {
        System.out.println("==从redis中get key="+key);
        logger.info("===从redis中get key="+key);


        Object result = null;
        JedisConnection connection = null;
        try {
            connection = jedisConnectionFactory.getConnection();
            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
            result = serializer.deserialize(connection.get(serializer.serialize(key)));
        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return result;

    }

    /**
     * As of 3.3.0 this method is only called during a rollback
     * for any previous value that was missing in the cache.
     * This lets any blocking cache to release the lock that
     * may have previously put on the key.
     * A blocking cache puts a lock when a value is null
     * and releases it when the value is back again.
     * This way other threads will wait for the value to be
     * available instead of hitting the database.
     *
     * @param key The key
     * @return Not used
     */
    @Override
    public Object removeObject(Object key) {

        JedisConnection connection = null;

        Object result = null;
        try
        {
            connection = jedisConnectionFactory.getConnection();
            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
            result =connection.expire(serializer.serialize(key), 0);
        }
        catch (JedisConnectionException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null) {
                connection.close();
            }
        }
        return result;

    }

    /**
     * Clears this cache instance
     */
    @Override
    public void clear() {
        JedisConnection connection = null;
        try {
            connection = jedisConnectionFactory.getConnection();
            connection.flushDb();
            connection.flushAll();
        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Optional. This method is not called by the core.
     *
     * @return The number of elements stored in the cache (not its capacity).
     */
    @Override
    public int getSize() {

        int result = 0;

        JedisConnection connection = null;
        try
        {
            connection = jedisConnectionFactory.getConnection();
            result = Integer.valueOf(connection.dbSize().toString());
        }
        catch (JedisConnectionException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null) {
                connection.close();
            }
        }
        return result;

    }

    /**
     * Optional. As of 3.2.6 this method is no longer called by the core.
     * <p>
     * Any locking needed by the cache must be provided internally by the cache provider.
     *
     * @return A ReadWriteLock
     */
    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    public static void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
        RedisCache.jedisConnectionFactory = jedisConnectionFactory;
    }
}

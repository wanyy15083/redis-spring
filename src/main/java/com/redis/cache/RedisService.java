package com.redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Redis加缓存的Service
 * 
 * @author Frotly
 *
 */
@Service
public class RedisService {

    //加载spring容器有该对象就注入，否则不注入
    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;

    /**
     * 新增数据
     * 
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value) {
        return execute(new Function<String, ShardedJedis>() {

            @Override
            public String callback(ShardedJedis shardedJedis) {
                return shardedJedis.set(key, value);
            }

        });
    }

    /**
     * 根据key获取值
     * 
     * @param key
     * @return
     */
    public String get(final String key) {
        return execute(new Function<String, ShardedJedis>() {

            @Override
            public String callback(ShardedJedis shardedJedis) {
                return shardedJedis.get(key);
            }

        });
    }

    /**
     * 根据key删除
     * 
     * @param key
     * @return
     */
    public Long del(final String key) {
        return execute(new Function<Long, ShardedJedis>() {

            @Override
            public Long callback(ShardedJedis shardedJedis) {
                return shardedJedis.del(key);
            }

        });
    }

    /**
     * 新增数据并设置存活时间
     * 
     * @param key
     * @param value
     * @param time
     * @return
     */
    public Long set(final String key, final String value, final Integer time) {
        return execute(new Function<Long, ShardedJedis>() {

            @Override
            public Long callback(ShardedJedis shardedJedis) {
                shardedJedis.set(key, value);
                return shardedJedis.expire(key, time);
            }

        });
    }

    /**
     * 根据key设置存活时间
     * 
     * @param key
     * @param time
     * @return
     */
    public Long set(final String key, final Integer time) {
        return execute(new Function<Long, ShardedJedis>() {

            @Override
            public Long callback(ShardedJedis shardedJedis) {
                return shardedJedis.expire(key, time);
            }

        });
    }

    private <T> T execute(Function<T, ShardedJedis> fun) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中取出jedis分片对象
            shardedJedis = this.shardedJedisPool.getResource();
            // 具体的业务逻辑
            return fun.callback(shardedJedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (shardedJedis != null) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
        return null;
    }
}

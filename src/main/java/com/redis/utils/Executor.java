package com.redis.utils;

import redis.clients.jedis.ShardedJedis;

/**
 */
public interface Executor<K> {
	public K execute(ShardedJedis jedis);
}

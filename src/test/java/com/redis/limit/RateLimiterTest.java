package com.redis.limit;

import com.google.common.cache.*;
import com.google.common.util.concurrent.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Created by songyigui on 2018/5/15.
 */
public class RateLimiterTest {
    private final static long limitSec = 100;

    private final LoadingCache<Long, AtomicLong> counter = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, AtomicLong>() {
                @Override
                public AtomicLong load(Long sec) throws Exception {
                    return new AtomicLong(0);
                }
            });

    public boolean limitTimeWindow() throws ExecutionException {
        long currentSec = System.currentTimeMillis() / 1000;
        if (counter.get(currentSec).incrementAndGet() > limitSec) {
            return false;
        }
        return true;
    }

    private RateLimiter rateLimiter = RateLimiter.create(limitSec);

    public boolean limitGuava () {
        rateLimiter.acquire();
        return rateLimiter.tryAcquire();
    }

}

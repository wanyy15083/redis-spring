package com.redis.limit;

import com.google.common.collect.*;
import com.redis.utils.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Created by songyigui on 2018/5/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-*.xml"})
public class RedisRateLimiterTest {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisScript<Long>   redisScript;

    @Test
    public void limitTimeWindow() throws Exception {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        for (int i = 0; i < 200; i++) {
            int currentSec = CommonUtil.getCurrentTimeStamp();
            String key = "limit_" + currentSec;
            Long increment = valueOperations.increment(key, 1);
            redisTemplate.expire(key, 2, TimeUnit.SECONDS);
            System.out.println(increment);
            if (increment >= 100) {
                break;
            }
            Thread.sleep(5);
        }
    }

    @Test
    public void limitTokenBucketLua() throws Exception {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<String, Object> map = new HashMap<>();
        map.put("curr_permits", "0");
        map.put("max_permits", "100000000");
        map.put("rate", "100000");
        hashOperations.putAll("token_limit_test", map);
        int runs = 7;
        final int it = 3000;
        for (int i = 0; i < runs; i++) {
            long start = System.currentTimeMillis();
            int tNum = 5;
            final AtomicLong count = new AtomicLong(0);
            ExecutorService executor = Executors.newFixedThreadPool(tNum);
            final CountDownLatch latch = new CountDownLatch(tNum);
            for (int j = 0; j < tNum; j++) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int k = 0; k < it; k++) {
                            int currentSec = CommonUtil.getCurrentTimeStamp();
                            Long acquire = redisTemplate.execute(redisScript, ImmutableList.of("token_limit_test"), "acquire", "1", currentSec + "", "");
                            long re = count.incrementAndGet();
                        }
                        latch.countDown();
                    }
                });
            }
            latch.await();
            long opsSec = (count.get() * 1000) / (System.currentTimeMillis() - start);
            System.out.format("Run %d, Lua=%,d ops/sec%n", i, opsSec);
        }

    }

    @Test
    public void limitIncrLua() throws Exception {
        for (int i = 0; i < 200; i++) {
            Long acquire = redisTemplate.execute(redisScript, ImmutableList.of("acquireIncr"), "acquireIncr", "", "", "3");
            System.out.println(acquire);
            Thread.sleep(new Random().nextInt(200));
        }

    }
}

package com.redis.utils;

/**
 * Created by bj-s2-w1631 on 18-8-13.
 */
public class CommonUtil {
    public static int getCurrentTimeStamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }
}

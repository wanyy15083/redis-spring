package com.redis.service;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by songyigui on 2016/8/11.
 */
public class SomeTest {

    public static void main(String[] args) {
        Set<String> s = new TreeSet<String>();
        Collections.addAll(s, args);
        System.out.println(s);
        System.out.println(System.nanoTime());
    }
}

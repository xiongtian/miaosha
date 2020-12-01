package com.xiongtian.miaosha.redis;

/**
 * @Author xiongtian
 * @Date 2020/12/1 21:00
 * @Version 1.0
 */

public class OrderKey extends BasePrefix {
    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}

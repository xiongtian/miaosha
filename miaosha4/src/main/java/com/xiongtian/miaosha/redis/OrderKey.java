package com.xiongtian.miaosha.redis;

/**
 * @Author xiongtian
 * @Date 2020/12/1 21:00
 * @Version 1.0
 */

public class OrderKey extends BasePrefix {

    public OrderKey( String prefix) {
        super( prefix);
    }


    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}

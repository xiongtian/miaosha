package com.xiongtian.miaosha.redis;

/**
 * @Author xiongtian
 * @Date 2020/12/1 21:00
 * @Version 1.0
 */

public class UserKey extends BasePrefix {


    private UserKey(String prefix) {
        super(prefix);
    }

    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKey getById = new UserKey("id");


    public static UserKey getByName = new UserKey("name");
}

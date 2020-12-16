package com.xiongtian.miaosha.access;

import com.xiongtian.miaosha.domain.MiaoshaUser;

/**
 * @Author xiongtian
 * @Date 2020/12/16 14:23
 * @Version 1.0
 */

public class UserContext {

    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user) {
        userHolder.set(user);
    }

    public static MiaoshaUser getUser() {
        return userHolder.get();
    }
}

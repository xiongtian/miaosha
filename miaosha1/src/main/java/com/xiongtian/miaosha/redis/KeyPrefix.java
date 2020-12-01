package com.xiongtian.miaosha.redis;

/**
 * @Author xiongtian
 * @Date 2020/12/1 20:53
 * @Version 1.0
 */

public interface KeyPrefix {

    /**
     * 失效时间
     * @return
     */
    public int expireSeconds();


    /**
     * 前缀
     * @return
     */
    public String getPrefix();



}

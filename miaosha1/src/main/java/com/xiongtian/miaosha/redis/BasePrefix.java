package com.xiongtian.miaosha.redis;

/**
 * @Author xiongtian
 * @Date 2020/12/1 20:56
 * @Version 1.0
 */

public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    /**
     * 默认过期时间为0，永不过期的方法
     * @param prefix
     */
    public BasePrefix( String prefix) {
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() { // 默认0，代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className +":"+prefix;
    }
}

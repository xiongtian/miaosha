package com.xiongtian.miaosha.redis;

/**
 * @Author xiongtian
 * @Date 2020/12/9 20:04
 * @Version 1.0
 */

public class MiaoshaKey extends BasePrefix{
    public MiaoshaKey(String prefix) {
        super(prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");

}

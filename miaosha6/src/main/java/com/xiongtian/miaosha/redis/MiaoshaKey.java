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
    public MiaoshaKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60,"mp");

}

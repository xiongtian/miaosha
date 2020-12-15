package com.xiongtian.miaosha.redis;

/**
 * @Author xiongtian
 * @Date 2020/12/9 20:04
 * @Version 1.0
 */

public class GoodsKey extends BasePrefix{
    public GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");
    public static GoodsKey getMiaoshaGoodsStock= new GoodsKey(0,"gs");

}

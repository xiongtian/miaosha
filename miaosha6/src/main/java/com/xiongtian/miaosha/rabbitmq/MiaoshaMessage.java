package com.xiongtian.miaosha.rabbitmq;

import com.xiongtian.miaosha.domain.MiaoshaUser;

/**
 * @Author xiongtian
 * @Date 2020/12/14 20:41
 * @Version 1.0
 */

public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}

package com.xiongtian.miaosha.vo;

import com.xiongtian.miaosha.domain.MiaoshaUser;

/**
 * @Author xiongtian
 * @Date 2020/12/10 20:16
 * @Version 1.0
 */

public class GoodsDetailVo {
    private MiaoshaUser user;
    private GoodsVo goods;
    private int miaoshaStatus;
    private int remainSeconds;

    public GoodsDetailVo() {
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
}

package com.xiongtian.miaosha.vo;

import com.xiongtian.miaosha.domain.Goods;
import com.xiongtian.miaosha.domain.OrderInfo;

/**
 * @Author xiongtian
 * @Date 2020/12/11 10:47
 * @Version 1.0
 */

public class OrderDetailVo {
    private Goods goods;
    private OrderInfo order;

    public OrderDetailVo() {
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}

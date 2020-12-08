package com.xiongtian.miaosha.service;

import com.xiongtian.miaosha.domain.MiaoshaOrder;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.OrderInfo;
import com.xiongtian.miaosha.vo.GoodsVo;

/**
 * @Author xiongtian
 * @Date 2020/12/8 20:25
 * @Version 1.0
 */

public interface OrderService {


    MiaoshaOrder getMiaoshaOrderByUserIdAndGoodsId(Long userId, long goodsId);

    OrderInfo createOrder(MiaoshaUser miaoshaUser, GoodsVo goods);
}

package com.xiongtian.miaosha.service.impl;

import com.xiongtian.miaosha.dao.GoodsDao;
import com.xiongtian.miaosha.domain.Goods;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.OrderInfo;
import com.xiongtian.miaosha.service.GoodsService;
import com.xiongtian.miaosha.service.MiaoshaService;
import com.xiongtian.miaosha.service.OrderService;
import com.xiongtian.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author xiongtian
 * @Date 2020/12/8 20:31
 * @Version 1.0
 */
@Service
public class MiaoshaServiceImpl implements MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Override
    @Transactional
    public OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goods) {

        // 减库存，下订单，写入秒杀订单

        goodsService.reduceStock(goods);
        //order_info miaosha_order两个表
        return orderService.createOrder(miaoshaUser, goods);
    }
}

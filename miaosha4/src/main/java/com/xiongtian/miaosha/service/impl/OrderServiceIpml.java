package com.xiongtian.miaosha.service.impl;

import com.xiongtian.miaosha.dao.OrderDao;
import com.xiongtian.miaosha.domain.MiaoshaOrder;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.OrderInfo;
import com.xiongtian.miaosha.service.OrderService;
import com.xiongtian.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author xiongtian
 * @Date 2020/12/8 20:26
 * @Version 1.0
 */

@Service
public class OrderServiceIpml implements OrderService {

    @Autowired
    OrderDao orderDao;

    /**
     * 判断用户有没有秒杀的商品
     * @param userId
     * @param goodsId
     * @return
     */
    @Override
    public MiaoshaOrder getMiaoshaOrderByUserIdAndGoodsId(Long userId, long goodsId) {


        return orderDao.getMiaoshaOrderByUserIdAndGoodsId(userId,goodsId);
    }

    /**
     * 生成订单
     * @param miaoshaUser
     * @param goods
     * @return
     */
    @Override
    @Transactional
    public OrderInfo createOrder(MiaoshaUser miaoshaUser, GoodsVo goods) {
        OrderInfo  orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(miaoshaUser.getId());
        long orderId=orderDao.insert(orderInfo);
        MiaoshaOrder miaoshaOrder =  new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(miaoshaUser.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        return orderInfo;
    }
}

package com.xiongtian.miaosha.service.impl;

import com.xiongtian.miaosha.dao.OrderDao;
import com.xiongtian.miaosha.domain.MiaoshaOrder;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.OrderInfo;
import com.xiongtian.miaosha.redis.OrderKey;
import com.xiongtian.miaosha.redis.RedisService;
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

    @Autowired
    RedisService redisService;


    /**
     * 判断用户有没有秒杀的商品
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @Override
    public MiaoshaOrder getMiaoshaOrderByUserIdAndGoodsId(Long userId, long goodsId) {
        //return orderDao.getMiaoshaOrderByUserIdAndGoodsId(userId,goodsId);
        //不存数据库中查而是从缓存中获取
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid, "" + userId + "_" + goodsId, MiaoshaOrder.class);
    }

    /**
     * 生成订单
     *
     * @param miaoshaUser
     * @param goods
     * @return
     */
    @Override
    @Transactional
    public OrderInfo createOrder(MiaoshaUser miaoshaUser, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(miaoshaUser.getId());
        // 插入之后mybatis会自动的将id塞入
        orderDao.insert(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(miaoshaUser.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);

        // 生存订单之后写入缓存
        redisService.set(OrderKey.getMiaoshaOrderByUidGid, "" + miaoshaOrder.getId() + "_" + goods.getId(), miaoshaOrder);

        return orderInfo;
    }

    @Override
    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}

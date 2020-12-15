package com.xiongtian.miaosha.service.impl;

import com.xiongtian.miaosha.dao.GoodsDao;
import com.xiongtian.miaosha.domain.Goods;
import com.xiongtian.miaosha.domain.MiaoshaOrder;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.OrderInfo;
import com.xiongtian.miaosha.redis.MiaoshaKey;
import com.xiongtian.miaosha.redis.RedisService;
import com.xiongtian.miaosha.service.GoodsService;
import com.xiongtian.miaosha.service.MiaoshaService;
import com.xiongtian.miaosha.service.OrderService;
import com.xiongtian.miaosha.util.MD5Util;
import com.xiongtian.miaosha.util.UUIDUtil;
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

    @Autowired
    RedisService redisService;

    @Override
    @Transactional
    public OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goods) {

        // 减库存，下订单，写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            //order_info miaosha_order两个表
            return orderService.createOrder(miaoshaUser, goods);
        } else {
            // 做出商品售光的标记
            setGoodsOver(goods.getId());
            return null;
        }

    }

    @Override
    public long getMiaoshaResult(Long id, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(id, goodsId);
        // 成功
        if (null != order) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                // 已售光
                return -1;
            } else {
                // 排队中
                return 0;
            }
        }
    }


    /**
     * 生成秒杀地址
     *
     * @param miaoshaUser
     * @param goodsId
     * @return
     */
    @Override
    public String createMiaoshaPath(MiaoshaUser miaoshaUser, long goodsId) {

        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(MiaoshaKey.getMiaoshaPath, "" + miaoshaUser.getId() + "_" + goodsId, str);
        return str;
    }

    /**
     * 验证秒杀地址
     *
     * @param miaoshaUser
     * @param goodsId
     * @param path
     * @return
     */
    @Override
    public boolean checkPath(MiaoshaUser miaoshaUser, long goodsId, String path) {
        if (null == miaoshaUser || null == path) {
            return false;
        }
        String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, "" + miaoshaUser.getId() + "_" + goodsId, String.class);
        return path.equals(pathOld);
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {

        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }


}

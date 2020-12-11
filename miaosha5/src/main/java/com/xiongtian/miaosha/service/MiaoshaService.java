package com.xiongtian.miaosha.service;

import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.OrderInfo;
import com.xiongtian.miaosha.vo.GoodsVo;

/**
 * @Author xiongtian
 * @Date 2020/12/8 20:31
 * @Version 1.0
 */

public interface MiaoshaService {
    OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goods);
}

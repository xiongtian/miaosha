package com.xiongtian.miaosha.service;

import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.OrderInfo;
import com.xiongtian.miaosha.vo.GoodsVo;

import java.awt.image.BufferedImage;

/**
 * @Author xiongtian
 * @Date 2020/12/8 20:31
 * @Version 1.0
 */

public interface MiaoshaService {
    OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goods);

    long getMiaoshaResult(Long id, long goodsId);

    boolean checkPath(MiaoshaUser miaoshaUser, long goodsId, String path);

    String createMiaoshaPath(MiaoshaUser miaoshaUser, long goodsId);

    BufferedImage createVerifyCode(MiaoshaUser miaoshaUser, long goodsId);

    boolean checkVerifyCode(MiaoshaUser miaoshaUser, long goodsId, int verifyCode);
}

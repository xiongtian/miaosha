package com.xiongtian.miaosha.service;

import com.xiongtian.miaosha.vo.GoodsVo;

import java.util.List;

/**
 * @Author xiongtian
 * @Date 2020/12/7 20:09
 * @Version 1.0
 */

public interface GoodsService {

    public List<GoodsVo> listGoodsVo();

    GoodsVo getGoodsVoByGoodsId(long goodsId);

    void reduceStock(GoodsVo goods);
}

package com.xiongtian.miaosha.service.impl;

import com.xiongtian.miaosha.dao.GoodsDao;
import com.xiongtian.miaosha.service.GoodsService;
import com.xiongtian.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author xiongtian
 * @Date 2020/12/7 20:10
 * @Version 1.0
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    public List<GoodsVo> listGoodsVo() {
        return goodsDao.getGoodsVoList();
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }


}
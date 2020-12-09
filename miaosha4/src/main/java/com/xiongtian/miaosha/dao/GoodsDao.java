package com.xiongtian.miaosha.dao;

import com.xiongtian.miaosha.domain.Goods;
import com.xiongtian.miaosha.domain.MiaoshaGoods;
import com.xiongtian.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author xiongtian
 * @Date 2020/12/7 20:11
 * @Version 1.0
 */
@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> getGoodsVoList();

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id=#{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id =#{goodsId }  ")
    int reduceStock( MiaoshaGoods g);
}

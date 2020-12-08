package com.xiongtian.miaosha.controller;

import com.xiongtian.miaosha.domain.MiaoshaOrder;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.OrderInfo;
import com.xiongtian.miaosha.result.CodeMessage;
import com.xiongtian.miaosha.service.GoodsService;
import com.xiongtian.miaosha.service.MiaoshaService;
import com.xiongtian.miaosha.service.OrderService;
import com.xiongtian.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author xiongtian
 * @Date 2020/12/7 21:24
 * @Version 1.0
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String doMiaosha(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") long goodsId) {
        if (null == miaoshaUser) {
            return "login";
        }
        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMessage.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }
        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
        if (null != order) {
            model.addAttribute("errmsg", CodeMessage.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }
        // 1、减库存 2、下订单 3、写入秒杀订单
        OrderInfo orderInfo=miaoshaService.miaosha(miaoshaUser,goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
}

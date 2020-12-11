package com.xiongtian.miaosha.controller;

import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.OrderInfo;
import com.xiongtian.miaosha.result.CodeMessage;
import com.xiongtian.miaosha.result.Result;
import com.xiongtian.miaosha.service.GoodsService;
import com.xiongtian.miaosha.service.OrderService;
import com.xiongtian.miaosha.vo.GoodsVo;
import com.xiongtian.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author xiongtian
 * @Date 2020/12/10 22:03
 * @Version 1.0
 */
@Controller
@RequestMapping("/order")
public class OrderController {


    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user, @RequestParam("orderId") long orderId) {
        if (null == user) {
            return Result.error(CodeMessage.SESSION_ERRO);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (null == order) {
            return Result.error(CodeMessage.Order_NOT_EXIST);
        }

        Long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }

}


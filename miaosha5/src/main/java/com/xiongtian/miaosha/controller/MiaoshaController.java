package com.xiongtian.miaosha.controller;

import com.xiongtian.miaosha.domain.MiaoshaOrder;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.OrderInfo;
import com.xiongtian.miaosha.rabbitmq.MQSender;
import com.xiongtian.miaosha.rabbitmq.MiaoshaMessage;
import com.xiongtian.miaosha.redis.GoodsKey;
import com.xiongtian.miaosha.redis.RedisService;
import com.xiongtian.miaosha.result.CodeMessage;
import com.xiongtian.miaosha.result.Result;
import com.xiongtian.miaosha.service.GoodsService;
import com.xiongtian.miaosha.service.MiaoshaService;
import com.xiongtian.miaosha.service.OrderService;
import com.xiongtian.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author xiongtian
 * @Date 2020/12/7 21:24
 * @Version 1.0
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;

    @RequestMapping("/do_miaosha1")
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
            model.addAttribute("errmsg", CodeMessage.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }
        // 1、减库存 2、下订单 3、写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }

    /**
     * 静态页面优化
     * Get和Post有什么区别？
     * Get是幂等的
     * Post不是幂等的
     *
     * @param model
     * @param miaoshaUser
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") long goodsId) {
        if (null == miaoshaUser) {
            return Result.error(CodeMessage.SESSION_ERRO);
        }
        // 预减库存
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) {
            return Result.error(CodeMessage.MIAOSHA_OVER);
        }
        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
        if (null != order) {
            return Result.error(CodeMessage.REPEATE_MIAOSHA);
        }
        // 入队
        MiaoshaMessage message = new MiaoshaMessage();
        message.setUser(miaoshaUser);
        message.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(message);
        // 返回排队中
        return Result.success(0);
        /*// 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return Result.error(CodeMessage.MIAOSHA_OVER);
        }
        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
        if (null != order) {
            return  Result.error(CodeMessage.REPEATE_MIAOSHA);
        }
        // 1、减库存 2、下订单 3、写入秒杀订单
        OrderInfo orderInfo=miaoshaService.miaosha(miaoshaUser,goods);

        return Result.success(orderInfo);*/
        // 加入rabbitmq优化
    }

    /**
     * orderId: 成功
     * -1：秒杀失败
     *0：排队中
     * @param model
     * @param miaoshaUser
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") long goodsId) {
        if (null == miaoshaUser) {
            return Result.error(CodeMessage.SESSION_ERRO);
        }
        long result = miaoshaService.getMiaoshaResult(miaoshaUser.getId(), goodsId);
        return Result.success(result);
    }


    /**
     * 系统初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (null == goodsVoList) {
            return;
        }
        // 系统启动时，将商品加载到缓存中去
        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
        }
    }
}

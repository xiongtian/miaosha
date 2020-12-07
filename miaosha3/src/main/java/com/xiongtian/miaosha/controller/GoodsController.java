package com.xiongtian.miaosha.controller;

import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.User;
import com.xiongtian.miaosha.redis.MiaoshaUserKey;
import com.xiongtian.miaosha.service.GoodsService;
import com.xiongtian.miaosha.service.MiaoshaUserService;
import com.xiongtian.miaosha.service.impl.MiaoshaUserServiceImpl;
import com.xiongtian.miaosha.vo.GoodsVo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @Author xiongtian
 * @Date 2020/12/3 20:59
 * @Version 1.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    private GoodsService goodsService;


    /**
     * 没使用UserArgumentResolvers进行校验
     */
    /* @RequestMapping("/to_list")
     public String toList(HttpServletResponse response, Model model,
                          @CookieValue(value = MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN,required = false) String cookieToken,
                          @RequestParam(value = MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN,required = false)String paramToken
     ) {
         if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
             return "login";
         }
         String token =StringUtils.isEmpty(paramToken)?cookieToken:paramToken;

         MiaoshaUser miaoshaUser = miaoshaUserService.getByToken(response,token);
         model.addAttribute("user",miaoshaUser);
         return "goods_list";

     }*/

    /**
     * 使用UserArgumentResolvers进行校验
     *
     * @param model
     * @param miaoshaUser
     * @return
     */
    @RequestMapping("/to_list")
    public String toList(Model model, MiaoshaUser miaoshaUser) {

        model.addAttribute("user", miaoshaUser);
        // 查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }


    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser miaoshaUser, @PathVariable("goodsId") long goodsId) {

        model.addAttribute("user", miaoshaUser);
        // 查询商品详细信息
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);
        // 判断当前的状态
        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;

        int remainSeconds = 0;

        if (now < startTime) {
            // 秒杀还没有开始，倒计时
            miaoshaStatus = 0;
            remainSeconds=(int)(startTime -now)/1000;
        } else if (now > endTime) {
            // 秒杀已结束
            miaoshaStatus = 2;
            remainSeconds=-1;
        } else {
            // 秒杀正在进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        return "goods_detail";
    }
}

package com.xiongtian.miaosha.controller;

import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.User;
import com.xiongtian.miaosha.redis.GoodsKey;
import com.xiongtian.miaosha.redis.MiaoshaUserKey;
import com.xiongtian.miaosha.redis.RedisService;
import com.xiongtian.miaosha.result.Result;
import com.xiongtian.miaosha.service.GoodsService;
import com.xiongtian.miaosha.service.MiaoshaUserService;
import com.xiongtian.miaosha.service.impl.MiaoshaUserServiceImpl;
import com.xiongtian.miaosha.vo.GoodsDetailVo;
import com.xiongtian.miaosha.vo.GoodsVo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
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
    RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
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
    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request,HttpServletResponse response,Model model, MiaoshaUser miaoshaUser) {
        model.addAttribute("user", miaoshaUser);
        // 从缓存中获取
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        // 查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        //return "goods_list";

        IWebContext ctx =new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        // 手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (null!=html){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail/{goodsId}",produces ="text/html" )
    @ResponseBody
    public String detail(HttpServletRequest request,HttpServletResponse response,Model model, MiaoshaUser miaoshaUser, @PathVariable("goodsId") long goodsId) {

        model.addAttribute("user", miaoshaUser);
        // 从缓存中获取
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        // 手动渲染
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
        IWebContext ctx =new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        // 手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (null!=html){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }
        return html;
    }

    /**
     * 页面静态化
     * @param request
     * @param response
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/detail/{goodsId}" )
    @ResponseBody
    public Result<GoodsDetailVo> detail2(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId) {


        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

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
        GoodsDetailVo detailVo = new GoodsDetailVo();
        detailVo.setGoods(goods);
        detailVo.setUser(user);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setMiaoshaStatus(miaoshaStatus);

        return Result.success(detailVo);
    }
}

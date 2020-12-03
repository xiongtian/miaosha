package com.xiongtian.miaosha.controller;

import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.User;
import com.xiongtian.miaosha.redis.MiaoshaUserKey;
import com.xiongtian.miaosha.service.MiaoshaUserService;
import com.xiongtian.miaosha.service.impl.MiaoshaUserServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

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
     * @param model
     * @param miaoshaUser
     * @return
     */
    @RequestMapping("/to_list")
    public String toList(Model model, MiaoshaUser miaoshaUser) {

        model.addAttribute("user", miaoshaUser);
        return "goods_list";

    }
}

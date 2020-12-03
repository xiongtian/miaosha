package com.xiongtian.miaosha.controller;

import com.xiongtian.miaosha.result.CodeMessage;
import com.xiongtian.miaosha.result.Result;
import com.xiongtian.miaosha.service.MiaoshaUserService;
import com.xiongtian.miaosha.util.ValidatorUtil;
import com.xiongtian.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author xiongtian
 * @Date 2020/12/2 20:57
 * @Version 1.0
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    /**
     * 跳转到登录页面
     *
     * @return
     */
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    /**
     * 登录的逻辑
     *
     * @return
     */
    @PostMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(LoginVo loginVo) {
        log.info(loginVo.toString());
        // 参数校验
        String passInpt = loginVo.getPassword();
        String mobile = loginVo.getMobile();
        if (StringUtils.isEmpty(passInpt)) {
            return Result.error(CodeMessage.PASSWORD_EMPTY);
        }
        if (StringUtils.isEmpty(mobile)) {
            return Result.error(CodeMessage.MOBILE_EMPTY);
        }
        if (!ValidatorUtil.isMobile(mobile)) {
            return Result.error(CodeMessage.MOBILE_ERROR);
        }
        //登录
        CodeMessage codeMessage = miaoshaUserService.login(loginVo);
        if (codeMessage.getCode() == 0) {

            log.info("用户："+mobile+"登录成功");
            return Result.success(true);

        } else {

            log.info("用户："+mobile+"登录失败");
            return Result.error(codeMessage);

        }
    }
}

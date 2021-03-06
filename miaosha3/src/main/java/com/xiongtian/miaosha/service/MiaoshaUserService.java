package com.xiongtian.miaosha.service;

import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.result.CodeMessage;
import com.xiongtian.miaosha.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author xiongtian
 * @Date 2020/12/2 22:33
 * @Version 1.0
 */

public interface MiaoshaUserService {

    public MiaoshaUser getById(Long id);

    boolean login(HttpServletResponse response, LoginVo loginVo);

    MiaoshaUser getByToken(HttpServletResponse response,String token);
}

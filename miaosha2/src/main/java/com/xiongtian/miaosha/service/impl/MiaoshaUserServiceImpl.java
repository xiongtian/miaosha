package com.xiongtian.miaosha.service.impl;

import com.xiongtian.miaosha.dao.MiaoshaUserDao;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.exception.GlobalException;
import com.xiongtian.miaosha.redis.MiaoshaUserKey;
import com.xiongtian.miaosha.redis.RedisService;
import com.xiongtian.miaosha.result.CodeMessage;
import com.xiongtian.miaosha.service.MiaoshaUserService;
import com.xiongtian.miaosha.util.MD5Util;
import com.xiongtian.miaosha.util.UUIDUtil;
import com.xiongtian.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.HttpResource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author xiongtian
 * @Date 2020/12/2 22:33
 * @Version 1.0
 */

@Service
public class MiaoshaUserServiceImpl implements MiaoshaUserService {


    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    @Override
    public MiaoshaUser getById(Long id) {

        return miaoshaUserDao.getById(id);
    }

    @Override
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (null == loginVo) {
            throw new GlobalException(CodeMessage.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.valueOf(mobile));
        if (null == user) {
            throw new GlobalException(CodeMessage.MOBILE_NOT_EXIT);
        }
        // 验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        // 根据输入的密码计算出的密码
        String calcPass = MD5Util.formPassToDBPass(password, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMessage.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        addCookie(response,token, user);

        return true;
    }

    @Override
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        // 延长有效期
        if (null != user) {
            addCookie(response,token, user);
        }
        return user;
    }


    private void addCookie(HttpServletResponse response,String token, MiaoshaUser user) {
        // 生成cookie
        //String token = UUIDUtil.uuid();
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

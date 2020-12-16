package com.xiongtian.miaosha.access;

import com.alibaba.fastjson.JSON;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.redis.AccessKey;
import com.xiongtian.miaosha.redis.RedisService;
import com.xiongtian.miaosha.result.CodeMessage;
import com.xiongtian.miaosha.result.Result;
import com.xiongtian.miaosha.service.MiaoshaUserService;
import com.xiongtian.miaosha.service.impl.MiaoshaUserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @Author xiongtian
 * @Date 2020/12/16 11:19
 * @Version 1.0
 */
@Component
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    RedisService redisService;

    /**
     * 方法执行之前
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            MiaoshaUser user = getMiaoshaUser(request, response);
            UserContext.setUser(user);

            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (null == accessLimit) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            String key = request.getRequestURI();
            if (needLogin) {
                if (null == user) {
                    render(response, CodeMessage.SESSION_ERRO);
                    return false;
                }
                key += "_" + user.getId();
            } else {
                //do nothing
            }
            AccessKey ak = AccessKey.withExpire(seconds);
            Integer count = redisService.get(ak, "" + key, Integer.class);

            if (count == null) {
                redisService.set(ak, "" + key, 1);
            } else if (count < maxCount) {
                redisService.incr(ak, "" + key);
            } else {
                render(response, CodeMessage.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }

    private void render(HttpServletResponse response, CodeMessage cm) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private MiaoshaUser getMiaoshaUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN);

        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return miaoshaUserService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}

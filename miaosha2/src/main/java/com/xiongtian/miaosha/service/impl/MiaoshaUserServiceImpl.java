package com.xiongtian.miaosha.service.impl;

import com.xiongtian.miaosha.dao.MiaoshaUserDao;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.result.CodeMessage;
import com.xiongtian.miaosha.service.MiaoshaUserService;
import com.xiongtian.miaosha.util.MD5Util;
import com.xiongtian.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author xiongtian
 * @Date 2020/12/2 22:33
 * @Version 1.0
 */

@Service
public class MiaoshaUserServiceImpl implements MiaoshaUserService {

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Override
    public MiaoshaUser getById(Long id) {

        return miaoshaUserDao.getById(id);
    }

    @Override
    public CodeMessage login(LoginVo loginVo) {
        if (null == loginVo) {
            return CodeMessage.SERVER_ERROR;
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.valueOf(mobile));
        if (null == user) {
            return CodeMessage.MOBILE_NOT_EXIT;
        }
        // 验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        // 根据输入的密码计算出的密码
        String calcPass = MD5Util.formPassToDBPass(password, saltDB);
        if (!calcPass.equals(dbPass)) {
            return CodeMessage.PASSWORD_ERROR;
        }

        return CodeMessage.SUCCESS;
    }
}

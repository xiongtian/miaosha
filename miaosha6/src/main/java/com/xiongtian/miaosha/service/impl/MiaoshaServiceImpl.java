package com.xiongtian.miaosha.service.impl;

import com.xiongtian.miaosha.dao.GoodsDao;
import com.xiongtian.miaosha.domain.Goods;
import com.xiongtian.miaosha.domain.MiaoshaOrder;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.domain.OrderInfo;
import com.xiongtian.miaosha.redis.MiaoshaKey;
import com.xiongtian.miaosha.redis.RedisService;
import com.xiongtian.miaosha.service.GoodsService;
import com.xiongtian.miaosha.service.MiaoshaService;
import com.xiongtian.miaosha.service.OrderService;
import com.xiongtian.miaosha.util.MD5Util;
import com.xiongtian.miaosha.util.UUIDUtil;
import com.xiongtian.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Author xiongtian
 * @Date 2020/12/8 20:31
 * @Version 1.0
 */
@Service
public class MiaoshaServiceImpl implements MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Override
    @Transactional
    public OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goods) {

        // 减库存，下订单，写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            //order_info miaosha_order两个表
            return orderService.createOrder(miaoshaUser, goods);
        } else {
            // 做出商品售光的标记
            setGoodsOver(goods.getId());
            return null;
        }

    }

    @Override
    public long getMiaoshaResult(Long id, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(id, goodsId);
        // 成功
        if (null != order) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                // 已售光
                return -1;
            } else {
                // 排队中
                return 0;
            }
        }
    }


    /**
     * 生成秒杀地址
     *
     * @param miaoshaUser
     * @param goodsId
     * @return
     */
    @Override
    public String createMiaoshaPath(MiaoshaUser miaoshaUser, long goodsId) {
        if (null == miaoshaUser || goodsId <= 0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(MiaoshaKey.getMiaoshaPath, "" + miaoshaUser.getId() + "_" + goodsId, str);
        return str;
    }

    /**
     * 验证秒杀地址
     *
     * @param miaoshaUser
     * @param goodsId
     * @param path
     * @return
     */
    @Override
    public boolean checkPath(MiaoshaUser miaoshaUser, long goodsId, String path) {
        if (null == miaoshaUser || null == path) {
            return false;
        }
        String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, "" + miaoshaUser.getId() + "_" + goodsId, String.class);
        return path.equals(pathOld);
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {

        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }

    /**
     * 生成验证码
     *
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId, rnd);
        //输出图片
        return image;
    }

    /**
     * script引擎
     *
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 静态数组
     */
    private static char[] ops = new char[]{'+', '-', '*'};

    /**
     * 生成数学公式的验证码
     * + - *
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    /**
     * 验证输入的结果是否正确
     *
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    @Override
    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }
        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId, Integer.class);
        if (null == codeOld || codeOld - verifyCode != 0){
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId);
        return true;
    }
}

package com.xiongtian.miaosha.rabbitmq;

import com.xiongtian.miaosha.domain.MiaoshaOrder;
import com.xiongtian.miaosha.domain.MiaoshaUser;
import com.xiongtian.miaosha.redis.RedisService;
import com.xiongtian.miaosha.service.GoodsService;
import com.xiongtian.miaosha.service.MiaoshaService;
import com.xiongtian.miaosha.service.OrderService;
import com.xiongtian.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author xiongtian
 * @Date 2020/12/11 20:37
 * @Version 1.0
 * <p>
 * Rabbitmq接受者
 */
@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    /**
     * Direct模式
     *
     * @param message
     */
    @RabbitListener(queues = MQConfig.QUEUE_NAME)
    public void receive(String message) {
        log.info("receive message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_NAME1)
    public void receiveTopic1(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("receive topicMessage1: " + msg);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_NAME2)
    public void receiveTopic2(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("receive topicMessage2: " + msg);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE_NAME)
    public void receicveHeaders(byte[] message) {

        log.info("receive headersMessage1: " + new String(message));
    }

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE_NAME)
    public void receicveMiaoshaQueue(String message) {
        log.info("receive miaomessage:" + message);
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();

        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (null != order) {
           return;
        }
        // 1、减库存 2、下订单 3、写入秒杀订单
        miaoshaService.miaosha(user,goods);
    }
}

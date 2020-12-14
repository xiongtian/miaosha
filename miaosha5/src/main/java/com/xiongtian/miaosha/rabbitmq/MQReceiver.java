package com.xiongtian.miaosha.rabbitmq;

import com.xiongtian.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @Author xiongtian
 * @Date 2020/12/11 20:37
 * @Version 1.0
 *
 * Rabbitmq接受者
 */
@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    /**
     * Direct模式
     * @param message
     */
    @RabbitListener(queues = MQConfig.QUEUE_NAME)
    public void receive(String message) {
        log.info("receive message:"+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_NAME1)
    public void receiveTopic1(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("receive topicMessage1: "+msg);
    }
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_NAME2)
    public void receiveTopic2(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("receive topicMessage2: "+msg);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE_NAME)
    public void receicveHeaders(byte[] message) {

        log.info("receive headersMessage1: "+new String(message));
    }
}

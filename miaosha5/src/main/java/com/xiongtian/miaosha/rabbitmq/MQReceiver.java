package com.xiongtian.miaosha.rabbitmq;

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
}

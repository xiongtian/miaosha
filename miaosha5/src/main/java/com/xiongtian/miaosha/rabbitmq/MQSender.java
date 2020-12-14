package com.xiongtian.miaosha.rabbitmq;

import com.xiongtian.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author xiongtian
 * @Date 2020/12/11 20:26
 * @Version 1.0
 * <p>
 * Rabbitmq发送者
 */

@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send message: "+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE_NAME,msg);
    }

    public void sendTopic(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send topicMessage: "+msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY1,message+"1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY2,message+"2");
    }



    public void sendFanout(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send fanoutMessage: "+msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",message);

    }

    public void sendHeaders(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send headersMessage: "+msg);
        MessageProperties properties =new MessageProperties();
        properties.setHeader("header1","value1");
        properties.setHeader("header2","value2");

        Message obj = new Message(msg.getBytes(),properties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",obj);

    }
}

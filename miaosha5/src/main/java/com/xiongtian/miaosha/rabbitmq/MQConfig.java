package com.xiongtian.miaosha.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @Author xiongtian
 * @Date 2020/12/11 20:38
 * @Version 1.0
 */
@Configuration
public class MQConfig {

    public static final String QUEUE_NAME = "queue";

    @Bean
    public Queue queue(){
        /*** name: 名称
         * durable：是否进行持久化 */
        return new Queue(QUEUE_NAME,true);
    }
}

package com.xiongtian.miaosha.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author xiongtian
 * @Date 2020/12/11 20:38
 * @Version 1.0
 */
@Configuration
public class MQConfig {
    public static final String MIAOSHA_QUEUE_NAME = "miaosha.queue";

    public static final String QUEUE_NAME = "queue";
    public static final String TOPIC_QUEUE_NAME1 = "topic.queue1";
    public static final String TOPIC_QUEUE_NAME2 = "topic.queue2";
    public static final String HEADERS_QUEUE_NAME = "headers.queue2";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String ROUTING_KEY1 = "topic.key1";
    public static final String ROUTING_KEY2 = "topic.#";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String HEADERS_EXCHANGE = "headersExchange";




    /**
     * Direct模式：交换机 exchange
     *
     */
    @Bean
    public Queue queue(){
        /*** name: 名称
         * durable：是否进行持久化 */
        return new Queue(QUEUE_NAME,true);
    }

    /**
     * Topic模式：交换机 exchange
     *
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE_NAME1,true);
    }

    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE_NAME2,true);
    }


    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
    }
    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
    }

    /*
    * Fanout模式：交换机Exchange
    *
    * */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    /*
     * Header模式：交换机Exchange
     *
     * */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    @Bean
    public Queue HeadersQueue(){
        return new Queue(HEADERS_QUEUE_NAME,true);
    }

    @Bean
    public Binding HeadersBinding(){
        Map<String,Object> map= new HashMap<>();
        map.put("header1","value1");
        map.put("header2","value2");

        return BindingBuilder.bind(HeadersQueue()).to(headersExchange()).whereAll(map).match();
    }


}

package com.xiongtian.miaosha.controller;

import com.xiongtian.miaosha.rabbitmq.MQSender;
import com.xiongtian.miaosha.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author xiongtian
 * @Date 2020/12/11 20:53
 * @Version 1.0
 */

@Controller
@RequestMapping("/sample")
public class SampleController {

    @Autowired
    MQSender mqSender;


    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){

        mqSender.send("hello,rabbitmq");
        return Result.success("hello,rabbitmq");
    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic(){

        mqSender.sendTopic("hello,rabbitmq");
        return Result.success("hello,rabbitmq");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout(){

        mqSender.sendFanout("hello,rabbitmq");
        return Result.success("hello,rabbitmq");
    }

    @RequestMapping("/mq/headers")
    @ResponseBody
    public Result<String> headers(){

        mqSender.sendHeaders("hello,rabbitmq");
        return Result.success("hello,rabbitmq");
    }
}

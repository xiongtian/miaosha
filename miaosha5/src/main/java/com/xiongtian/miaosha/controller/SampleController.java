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
}

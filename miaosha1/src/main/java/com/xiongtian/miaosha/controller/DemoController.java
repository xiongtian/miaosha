package com.xiongtian.miaosha.controller;

import com.xiongtian.miaosha.domain.User;
import com.xiongtian.miaosha.result.CodeMessage;
import com.xiongtian.miaosha.result.Result;
import com.xiongtian.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/demo")
@Controller
public class DemoController {

    @Autowired
    private UserService userService;


    @RequestMapping("/")
    @ResponseBody
    String home(){
        return "hello world!";
    }


    @RequestMapping("/hello")
    @ResponseBody
   public Result<String> hello(){
        //return new Result(0,"success","hello BONC !");
        return Result.success("hello BONC!");
    }

    @RequestMapping("/helloErro")
    @ResponseBody
    public Result<String>  helloErro(){
        //return new Result(500,"session 失效","hello BONC !");
        return Result.error(CodeMessage.SERVER_ERROR);
    }

    /**
     * 注意：解析thymeleaf时不能添加@ResponseBody
     * @param model
     * @return
     */
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name","xiongtian");
        return "hello";
    }


    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet(){
        User userById = userService.getUserById(1);
        return Result.success(userById);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.tx(1);
        return Result.success(true);
    }
}

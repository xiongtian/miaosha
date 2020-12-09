package com.xiongtian.miaosha.result;

/**
 * 错误信息
 */
public class CodeMessage {

    private int code;

    private String msg;

    // 通用异常
    public static CodeMessage SUCCESS = new CodeMessage(0, "success");
    public static CodeMessage SERVER_ERROR = new CodeMessage(500100, "服务端异常！");
    public static CodeMessage BIND_ERROR = new CodeMessage(500101, "参数校验异常: %s");


    // 登录模块：500,2XX
    public static CodeMessage SESSION_ERRO = new CodeMessage(500210, "Session不存在或是已经失效！");
    public static CodeMessage PASSWORD_EMPTY = new CodeMessage(500211, "登录密码不能为空！");
    public static CodeMessage MOBILE_EMPTY = new CodeMessage(500212, "手机号不能为空！");
    public static CodeMessage MOBILE_ERROR = new CodeMessage(500213, "手机号码格式错误！");
    public static CodeMessage MOBILE_NOT_EXIT = new CodeMessage(500214, "手机号不存在！");
    public static CodeMessage PASSWORD_ERROR = new CodeMessage(500215, "密码或账号错误！");

    // 商品模块：500,3XX

    // 订单模块：500,4XX

    // 秒杀模块：500,5XX
    public static CodeMessage MIAOSHA_OVER = new CodeMessage(500500, "商品已经秒杀完毕！");
    public static CodeMessage REPEATE_MIAOSHA = new CodeMessage(500501, "不能重复秒杀！");

    public CodeMessage() {
    }

    private CodeMessage(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMessage fillArgs(Object... args){

        int code = this.code;
        String message = String.format(this.msg,args);
        return new CodeMessage(code,message);
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

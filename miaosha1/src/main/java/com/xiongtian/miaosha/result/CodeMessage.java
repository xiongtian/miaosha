package com.xiongtian.miaosha.result;

/**
 * 错误信息
 */
public class CodeMessage {

    private int code;

    private String msg;

    // 通用异常
    public static CodeMessage SUCCESS = new CodeMessage(0,"success");
    public static CodeMessage SERVER_ERROR = new CodeMessage(500,"服务端异常！");

    // 登录模块：500,2XX

    // 商品模块：500,3XX

    // 订单模块：500,4XX

    // 秒杀模块：500,5XX

    public CodeMessage() {
    }

    private CodeMessage(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

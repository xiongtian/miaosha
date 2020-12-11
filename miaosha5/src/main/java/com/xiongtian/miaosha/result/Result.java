package com.xiongtian.miaosha.result;

/**
 * 响应返回信息
 *
 * @param <T>
 */
public class Result<T> {

    /*** 状态码*/
    private int code;

    /*** 信息*/
    private String msg;

    /*** 数据*/
    private T data;

    public Result() {
    }

    public Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    public Result(CodeMessage codeMessage) {
        if (null == codeMessage) {
           return;
        }
        this.code = codeMessage.getCode();
        this.msg = codeMessage.getMsg();
    }


    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public int getCode() {
        return code;
    }



    public String getMsg() {
        return msg;
    }



    public T getData() {
        return data;
    }



    /**
     * 成功时候的调用
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    /**
     * 失败时候的调用
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMessage codeMessage) {
        return new Result<T>(codeMessage);
    }
}

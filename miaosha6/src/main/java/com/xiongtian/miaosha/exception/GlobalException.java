package com.xiongtian.miaosha.exception;

import com.xiongtian.miaosha.result.CodeMessage;

/**
 * @Author xiongtian
 * @Date 2020/12/3 14:08
 * @Version 1.0
 */

public class GlobalException extends RuntimeException {


    private static final long serialVersionUID = 1L;


    private CodeMessage codeMessage;

    public GlobalException(CodeMessage codeMessage) {

        super(codeMessage.toString());
        this.codeMessage = codeMessage;
    }

    public CodeMessage getCodeMessage() {
        return codeMessage;
    }

}

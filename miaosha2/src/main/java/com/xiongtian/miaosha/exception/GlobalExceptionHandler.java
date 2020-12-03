package com.xiongtian.miaosha.exception;

import com.xiongtian.miaosha.result.CodeMessage;
import com.xiongtian.miaosha.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author xiongtian
 * @Date 2020/12/3 12:38
 * @Version 1.0
 */

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        if (e instanceof GlobalException) {
            GlobalException exception = (GlobalException) e;
            return Result.error(exception.getCodeMessage());
        } else if (e instanceof BindException) {
            BindException bind = (BindException) e;
            List<ObjectError> allErrors = bind.getAllErrors();
            ObjectError error = allErrors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMessage.BIND_ERROR.fillArgs(msg));
        } else {
            System.out.println("123456");
            return Result.error(CodeMessage.SERVER_ERROR);
        }
    }
}

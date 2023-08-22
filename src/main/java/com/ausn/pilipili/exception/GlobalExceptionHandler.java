package com.ausn.pilipili.exception;

import com.ausn.pilipili.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/8 1:12
 * @Description:
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return Result.fail(message);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = {Exception.class})
    public Result handleException(Exception e)
    {
        log.error("异常发生了: ", e);
        return Result.fail("服务器发生了错误："+e.getMessage());
    }
}
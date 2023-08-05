package com.ausn.pilipili.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result
{
    private int code;
    private String message;
    private Object data;

    public Result(int code, String message, Object data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    static public Result ok()
    {
        return new Result(ResultCode.DEFAULT_OK,null,null);
    }
    static public Result ok(int code)
    {
        return new Result(code,null,null);
    }
    static public Result ok(int code,Object data)
    {
        return new Result(code,null,data);
    }
    static public Result fail(String message)
    {
        return new Result(ResultCode.DEFAULT_ERR,message,null);
    }
    static public Result fail(int code,String message)
    {
        return new Result(code,message,null);
    }
}

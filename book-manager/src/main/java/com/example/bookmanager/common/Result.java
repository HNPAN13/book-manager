package com.example.bookmanager.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;   // 200成功，500失败
    private String msg;
    private T data;

    // 成功
    public static <T> Result<T> success(T data){
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMsg("操作成功");
        r.setData(data);
        return r;
    }
    public static <T> Result<T> success(){
        return success(null);
    }

    // 失败（默认500）
    public static <T> Result<T> fail(String msg){
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    // 新增：自定义code、msg、data，给拦截器401使用
    public static <T> Result<T> build(int code, String msg, T data){
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}

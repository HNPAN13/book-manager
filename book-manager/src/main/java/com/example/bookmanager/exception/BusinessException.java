package com.example.bookmanager.exception;

/**
 * 自定义业务异常，业务逻辑直接throw抛出
 */
public class BusinessException extends RuntimeException{
    public BusinessException(String message){
        super(message);
    }
}

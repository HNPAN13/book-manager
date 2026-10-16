package com.example.bookmanager.exception;

import com.example.bookmanager.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局统一异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获全部通用异常（500）
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        log.error("系统发生异常：",e);
        return Result.fail("服务器内部异常，请联系管理员");
    }

    /**
     * 空指针异常单独处理
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<?> handleNullPointer(NullPointerException e){
        log.error("空指针异常：",e);
        return Result.fail("数据为空，操作失败");
    }

    /**
     * 参数非法异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArg(IllegalArgumentException e){
        log.warn("参数异常：{}",e.getMessage());
        return Result.fail(e.getMessage());
    }
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusiness(BusinessException e){
        return Result.fail(e.getMessage());
    }

}

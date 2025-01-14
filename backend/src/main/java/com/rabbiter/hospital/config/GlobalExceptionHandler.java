package com.rabbiter.hospital.config;

import com.rabbiter.hospital.utils.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger Log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理请求参数缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseData handleMissingParams(MissingServletRequestParameterException ex) {
        Log.error("请求参数缺失: {}", ex.getParameterName());
        return ResponseData.fail("缺少必需的参数: " + ex.getParameterName());
    }
    
    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseData handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Log.error("参数类型不匹配: {} (需要类型: {})", ex.getName(), ex.getRequiredType().getSimpleName());
        return ResponseData.fail("参数'" + ex.getName() + "'的类型不正确");
    }
    
    /**
     * 处理所有其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseData handleException(Exception ex) {
        Log.error("发生未处理的异常: ", ex);
        return ResponseData.fail("服务器内部错误: " + ex.getMessage());
    }
}

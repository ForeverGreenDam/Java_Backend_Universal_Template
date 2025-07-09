package com.greendam.template.common.utils;

import com.greendam.template.exception.BusinessException;
import com.greendam.template.exception.ErrorCode;

/**
 * 异常处理工具类
 * @author ForeverGreenDam
 */
public class ThrowUtils {
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition,new BusinessException(errorCode));
    }
    public static void throwIf(boolean condition,ErrorCode errorCode,String message) {
        throwIf(condition,new BusinessException(errorCode,message));
    }
}

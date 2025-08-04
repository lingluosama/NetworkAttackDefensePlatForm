package com.lingluo.attackdefendplatform.exception;

import com.lingluo.attackdefendplatform.common.result.IResultCode;
import com.lingluo.attackdefendplatform.exception.BusinessException;
import lombok.Getter;

@Getter
public enum SystemErrorType implements IResultCode {
    RECORD_IS_IN_AUDITING("B001", "记录正在审核中，请刷新页面"),

    ;

    // 错误码
    private final String code;
    // 错误信息
    private final String msg;

    // 构造函数
    SystemErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 将枚举项转换为BusinessException
     */
    public BusinessException toException() {
        return new BusinessException(this);
    }
}
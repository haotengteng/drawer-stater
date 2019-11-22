package com.bee.ms.drawer.exception;

import com.bee.ms.common.exception.CustomSystemException;
import lombok.extern.slf4j.Slf4j;

/**
 * 参数自定义异常
 *
 * @author Created by haoteng on 2018/6/29.
 */
@Slf4j
public class ParamException extends CustomSystemException {


    public ParamException() {
        super(ErrorCodeEnum.ERROR_INTERNAL_ERROR.getCode(), ErrorCodeEnum.ERROR_INTERNAL_ERROR.getMessage());
    }

    public ParamException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getCode(), errorCodeEnum.getMessage());
    }

}

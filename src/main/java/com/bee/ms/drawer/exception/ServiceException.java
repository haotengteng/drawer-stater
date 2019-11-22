package com.bee.ms.drawer.exception;

import com.bee.ms.common.exception.CustomSystemException;

/**
 * @author Created by haoteng on 2018/6/29.
 */
public class ServiceException extends CustomSystemException {
    public ServiceException() {
        super(ErrorCodeEnum.ERROR_INTERNAL_ERROR.getCode(), ErrorCodeEnum.ERROR_INTERNAL_ERROR.getMessage());
    }

    public ServiceException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getCode(), errorCodeEnum.getMessage());
    }
    public ServiceException(Integer errorCode ,String errorMessage) {
        super(errorCode, errorMessage);
    }
}

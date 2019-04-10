package com.bs.lk.newoamvptest.base.mvp;

/**
 * @Description API网络请求异常
 * @author lk
 */
public  class ApiException extends RuntimeException {
    private int mErrorCode;
    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }
}

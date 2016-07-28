package com.yu.retrofittest.rx;

/**
 * 用于rxjava+retrofit网络请求不成功时抛出，可以在onError回调中取得code和message
 */
public class ApiException extends IllegalStateException {

    private int code;

    public ApiException(int code) {
        super();
        this.code = code;
    }

    public ApiException(int code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

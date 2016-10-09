package com.yu.retrofittest.rx;


public class ApiException extends RuntimeException {

    public int code;
    public String message;

    public ApiException(int code, String detailMessage) {
        super(detailMessage);
        this.code = code;
        this.message = detailMessage;
    }
}

package com.yu.falcon.callback;

import okhttp3.Request;
import okhttp3.Response;

public abstract class Callback<T> {

    public void onBefore(Request request) {
    }

    public void inProgress(float progress, long total) {
    }

    public abstract void onResponse(T response);

    public abstract void onError(Request request, Response response);

    public void onAfter() {
    }

    /**
     * 校验业务逻辑是否成功
     */
    public boolean validateReponse(Response response) {
        return response.isSuccessful();
    }

    /**
     * 解析response,返回的结果直接回调到onResponse
     */
    public abstract T parseNetworkResponse(Response response) throws Exception;
}
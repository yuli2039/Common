package com.yu.falcon.core;

import com.yu.falcon.Falcon;
import com.yu.falcon.builder.AbsRequestBuilder;
import com.yu.falcon.callback.Callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author yu
 *         Create on 16/7/20.
 */
public class RequestWrapper {

    private AbsRequestBuilder builder;

    public RequestWrapper(AbsRequestBuilder builder) {
        this.builder = builder;
    }

    /**
     * 同步请求
     */
    public Response execute() throws IOException {
        Request request = builder.generateRequest(null);
        Call call = Falcon.getInstance().getOkHttpClient().newCall(request);
        return call.execute();
    }

    /**
     * 异步请求
     */
    public void enqueue(Callback callback) {
        Request request = builder.generateRequest(callback);
        Falcon.getInstance().enqueue(request, callback);
    }
}

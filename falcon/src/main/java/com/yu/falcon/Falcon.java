package com.yu.falcon;

import com.yu.falcon.builder.GetBuilder;
import com.yu.falcon.builder.HeadBuilder;
import com.yu.falcon.builder.OtherRequestBuilder;
import com.yu.falcon.builder.PostFileBuilder;
import com.yu.falcon.builder.PostFormBuilder;
import com.yu.falcon.builder.PostStringBuilder;
import com.yu.falcon.callback.Callback;
import com.yu.falcon.core.Platform;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author yu
 *         Create on 16/7/20.
 */
public class Falcon {

    private volatile static Falcon mInstance;
    private OkHttpClient mClient;
    private Platform mPlatform;

    private Falcon(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mClient = new OkHttpClient();
        } else {
            mClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }

    public static Falcon initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (Falcon.class) {
                if (mInstance == null) {
                    mInstance = new Falcon(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static Falcon getInstance() {
        return initClient(null);
    }

    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return mClient;
    }

    // *****请求入口************************************************
    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public void enqueue(final Request request, final Callback callback) {
        if (callback == null) throw new IllegalArgumentException("the callback can not be null !");

        callback.onBefore(request);
        Call call = mClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailResult(request, null, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (call.isCanceled()) {
                        // 如果请求cancel，只回调onAfter
                        sendOnAfter(callback);
                        return;
                    }

                    if (!callback.validateReponse(response)) {
                        sendFailResult(request, response, callback);
                        return;
                    }

                    Object o = callback.parseNetworkResponse(response);
                    sendSuccessResult(o, callback);
                } catch (Exception e) {
                    sendFailResult(request, null, callback);
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }
            }
        });
    }


    private void sendOnAfter(final Callback callback) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onAfter();
            }
        });
    }

    private void sendFailResult(final Request request, final Response r, final Callback callback) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, r);
                callback.onAfter();
            }
        });
    }

    private void sendSuccessResult(final Object object, final Callback callback) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public void cancelByTag(Object tag) {
        for (Call call : mClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}

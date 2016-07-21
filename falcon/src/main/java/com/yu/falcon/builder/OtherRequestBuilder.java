package com.yu.falcon.builder;

import com.yu.falcon.Falcon;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;

/**
 * @author yu
 *         Create on 16/7/19.
 */
public class OtherRequestBuilder extends AbsRequestBuilder<OtherRequestBuilder> {

    private RequestBody requestBody;
    private String method;

    public OtherRequestBuilder(String method) {
        this.method = method;
    }

    public OtherRequestBuilder requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public OtherRequestBuilder requestBody(String content) {
        if (content != null)
            requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), content);
        return this;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (requestBody == null && HttpMethod.requiresRequestBody(method)) {
            throw new IllegalArgumentException("requestBody can not be null in method:" + method);
        }
        return requestBody;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        switch (method) {
            case Falcon.METHOD.PUT:
                builder.put(requestBody);
                break;
            case Falcon.METHOD.HEAD:
                builder.head();
                break;
            case Falcon.METHOD.PATCH:
                builder.patch(requestBody);
                break;
            case Falcon.METHOD.DELETE:
                if (requestBody == null)
                    builder.delete();
                else
                    builder.delete(requestBody);
                break;
        }
        return builder.build();
    }
}

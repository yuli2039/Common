package com.yu.falcon.builder;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author yu
 *         Create on 16/7/19.
 */
public class PostStringBuilder extends AbsRequestBuilder<PostStringBuilder> {

    private String content;
    private MediaType mediaType = MediaType.parse("text/plain;charset=utf-8");

    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (content == null)
            throw new IllegalArgumentException("the content can not be null !");
        return RequestBody.create(mediaType, content);
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }
}

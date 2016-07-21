package com.yu.falcon.builder;

import com.yu.falcon.callback.Callback;
import com.yu.falcon.Falcon;
import com.yu.falcon.core.CountingRequestBody;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author yu
 *         Create on 16/7/19.
 */
public class PostFileBuilder extends AbsRequestBuilder<PostFileBuilder> {

    private File file;
    private MediaType mediaType = MediaType.parse("application/octet-stream");

    public PostFileBuilder file(File file) {
        this.file = file;
        return this;
    }

    public PostFileBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (this.file == null) {
            throw new IllegalArgumentException("the file can not be null !");
        }
        return RequestBody.create(mediaType, file);
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody body, final Callback callback) {
        if (callback == null) return body;
        return new CountingRequestBody(body, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength) {

                Falcon.getInstance().getDelivery().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.inProgress(bytesWritten * 1.0f / contentLength, contentLength);
                    }
                });

            }
        });
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }
}

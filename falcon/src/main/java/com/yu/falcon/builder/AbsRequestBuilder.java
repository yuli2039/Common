package com.yu.falcon.builder;

import android.text.TextUtils;

import com.yu.falcon.callback.Callback;
import com.yu.falcon.core.RequestWrapper;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 对OkHttp的Request.Builder进行一个包装
 */
public abstract class AbsRequestBuilder<R extends AbsRequestBuilder> {

    protected String url;
    protected Map<String, String> params;
    protected Map<String, String> pathParams;// 用于url中的{path}参数

    protected Request.Builder builder = new Request.Builder();

    public R url(String url) {
        this.url = url;
        return (R) this;
    }

    public R tag(Object tag) {
        builder.tag(tag);
        return (R) this;
    }

    public R addHeader(String key, String val) {
        builder.header(key, val);
        return (R) this;
    }

    public R headers(Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            Headers.Builder hb = new Headers.Builder();
            for (Map.Entry<String, String> en : headers.entrySet()) {
                hb.add(en.getKey(), en.getValue());
            }
            builder.headers(hb.build());
        }
        return (R) this;
    }

    public R pathParameter(Map<String, String> paths) {
        this.pathParams = paths;
        return (R) this;
    }

    public R pathParameters(String key, String val) {
        if (this.pathParams == null)
            pathParams = new LinkedHashMap<>();
        pathParams.put(key, val);
        return (R) this;
    }

    /**
     * 设置url中的{path}参数
     */
    protected void setPathParameter() {
        if (pathParams != null) {
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                url = url.replace("{" + entry.getKey() + "}", entry.getValue());
            }
            if (url.contains("{") || url.contains("}")) {
                throw new IllegalArgumentException("the url contains \'{\' or \'}\', please set path parameter.");
            }
        }
        builder.url(url);
    }

    /**
     * 生成request包装类
     */
    public RequestWrapper build() {
        if (TextUtils.isEmpty(url))
            throw new IllegalArgumentException("the url can not be null !");
        if (url.contains("{") || url.contains("}"))
            setPathParameter();
        return new RequestWrapper(this);
    }

    protected abstract RequestBody buildRequestBody();

    /**
     * 包装requestBody，现仅用于上传文件和表单时的进度回调,其他请求直接返回body
     */
    protected RequestBody wrapRequestBody(RequestBody body, Callback callback) {
        return body;
    }

    protected abstract Request buildRequest(RequestBody requestBody);

    /**
     * RequestWrapper中调用此方法，生成最终的request
     */
    public Request generateRequest(Callback callback) {
        RequestBody requestBody = buildRequestBody();
        RequestBody wrappedRequestBody = wrapRequestBody(requestBody, callback);
        return buildRequest(wrappedRequestBody);
    }

}

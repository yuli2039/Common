package com.yu.falcon.builder;

import android.net.Uri;

import com.yu.falcon.core.Paramsable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author yu
 *         Create on 16/7/19.
 */
public class GetBuilder extends AbsRequestBuilder<GetBuilder> implements Paramsable {

    @Override
    public AbsRequestBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public AbsRequestBuilder addParams(String key, String val) {
        if (params == null)
            params = new LinkedHashMap<>();
        params.put(key, val);
        return this;
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        if (params != null)
            url = appendParams(url, params);
        return builder.get().build();
    }

    /**
     * 添加查询参数
     */
    protected String appendParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }
}

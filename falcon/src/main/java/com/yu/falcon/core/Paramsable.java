package com.yu.falcon.core;

import com.yu.falcon.builder.AbsRequestBuilder;

import java.util.Map;

/**
 * 请求是否有body参数
 */
public interface Paramsable {
    AbsRequestBuilder addParams(String key, String val);

    AbsRequestBuilder params(Map<String, String> params);
}

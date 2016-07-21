package com.yu.falcon.builder;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author yu
 *         Create on 16/7/19.
 */
public class HeadBuilder extends GetBuilder {

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.head().build();
    }
}

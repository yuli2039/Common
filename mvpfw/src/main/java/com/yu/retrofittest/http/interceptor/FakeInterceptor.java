/*
 * Copyright (C) 2016. Tien Hoang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yu.retrofittest.http.interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 模拟接口返回数据
 */
public class FakeInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpUrl url = chain.request().url();
        return new Response.Builder()
                .code(200)
                .message("request success")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), getResponseString(url).getBytes()))
                .addHeader("content-type", "application/json")
                .build();
    }

    /**
     * 根据url返回模拟的json字符串
     */
    private String getResponseString(HttpUrl url) {
        return "";
    }

}

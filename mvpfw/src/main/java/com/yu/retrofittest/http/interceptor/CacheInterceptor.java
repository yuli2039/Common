package com.yu.retrofittest.http.interceptor;

import com.yu.retrofittest.util.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 缓存控制,okhttpClient必须同时设置cache才能生效
 *
 * @author yu
 *         Create on 16/7/13.
 */
public class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetUtils.isOnline()) {
            // 没有网络时强制使用缓存,即时缓存已过期,如果没有缓存,会返回504 Unsatisfiable Request
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (NetUtils.isOnline()) {
            //有网的时候读服务器配置，也可以在这里进行统一的设置为0
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
//                    .header("Cache-Control", "public, max-age=0") // 设置为0
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        } else {
            // 没有网络时，读取缓存，缓存4周
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .removeHeader("Pragma")
                    .build();
        }
    }

}

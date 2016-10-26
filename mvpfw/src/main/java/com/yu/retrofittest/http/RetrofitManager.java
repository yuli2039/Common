package com.yu.retrofittest.http;

import com.yu.retrofittest.http.interceptor.HeaderInterceptor;
import com.yu.retrofittest.http.interceptor.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yu
 *         Create on 16/5/19.
 */
public class RetrofitManager {

    private static final String BASE_URL = "http://stage.ypd.host:8888/gateway/";
    private static final long DEFAULT_TIMEOUT = 10_000L;

    private Retrofit retrofit;

    private RetrofitManager() {
//        InputStream in = null;// TODO 拿到证书的输入流
//        HttpsManager.SSLParams sslp = HttpsManager.getSslSocketFactory(new InputStream[]{in}, null, null);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("_okhttp",true))
                .addInterceptor(new HeaderInterceptor())
//                .sslSocketFactory(sslp.sSLSocketFactory)
//                .addInterceptor(new GzipRequsetInterceptor())
//                .addInterceptor(new CacheInterceptor())// cache拦截器必须配合cache目录才生效
//                .cache(new Cache(Environment.getExternalStorageDirectory(), 10 * 1024 * 1024))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private static class SingletonHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <T> T createService(Class<T> clazz) {
        return retrofit.create(clazz);
    }

}

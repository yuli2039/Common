package com.yu.retrofittest.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yu
 *         Create on 16/5/19.
 */
public class HttpMethods {

    private static final String BASE_URL = "http://stage.ypd.host:8888/gateway/";
    private static final long DEFAULT_TIMEOUT = 10_000L;

    private Retrofit retrofit;

    //构造方法私有
    private HttpMethods() {
//        InputStream in = null;// TODO 拿到证书的输入流
//        HttpsManager.SSLParams sslp = HttpsManager.getSslSocketFactory(new InputStream[]{in}, null, null);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
//                .sslSocketFactory(sslp.sSLSocketFactory)
                .addInterceptor(new LoggerInterceptor("yuu"))
                .addInterceptor(new HeaderInterceptor())
//                .addInterceptor(new GzipRequsetInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <T> T createService(Class<T> clazz) {
        return retrofit.create(clazz);
    }

}

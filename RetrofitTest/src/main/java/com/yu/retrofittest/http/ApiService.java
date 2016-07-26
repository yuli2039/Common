package com.yu.retrofittest.http;

import com.yu.retrofittest.GankEn;
import com.yu.retrofittest.LoginEn;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiService {

    @POST("user/v0/oauth2/access_token")
    Observable<LoginEn> login(@Body Map<String, String> params);

    @GET("http://gank.io/api/search/query/listview/category/Android/count/{count}/page/{page}")
    Observable<GankEn> gank(@Path("count") int count, @Path("page") int page);

}
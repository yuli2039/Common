package com.yu.retrofittest.http;

import com.yu.retrofittest.entity.TestEn;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiService {

    @GET("http://gank.io/api/data/Android/{pageNum}/{page}")
    Observable<TestEn> login(@Path("pageNum") String pageNum, @Path("page") String page);

}
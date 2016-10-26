package com.yu.retrofittest.http;

import com.yu.retrofittest.entity.GankEntity;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiService {

    @GET("http://gank.io/api/data/Android/{pageNum}/{page}")
    Observable<HttpResult<List<GankEntity>>> gank(@Path("pageNum") String pageNum, @Path("page") String page);

}
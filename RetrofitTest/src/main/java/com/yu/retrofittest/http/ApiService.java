package com.yu.retrofittest.http;

import com.yu.retrofittest.LoginEn;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiService {

    @POST("user/v0/oauth2/access_token")
    Observable<LoginEn> login(@Body Map<String, String> params);
}
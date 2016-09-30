package com.yu.retrofittest.http.convert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 自定义Converter便于预处理服务端返回的数据
 *
 * @author yu
 *         Create on 16/9/30.
 */
public class ApiConverterFactory extends Converter.Factory {

    private final boolean flag;

    public static ApiConverterFactory create(boolean flag) {
        return create(new Gson(), flag);
    }

    public static ApiConverterFactory create(Gson gson, boolean flag) {
        return new ApiConverterFactory(gson, flag);
    }

    private final Gson gson;

    /**
     * flag 表示服务端返回的json格式, 作为json预处理的判断依据
     *
     * @param flag true:返回的json是三段式(code,message,data) false:所有字段和code平级,不放在data里面
     */
    private ApiConverterFactory(Gson gson, boolean flag) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
        this.flag = flag;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new ApiResponseConverter<>(gson, adapter, flag);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new ApiRequestConverter<>(gson, adapter);
    }
}

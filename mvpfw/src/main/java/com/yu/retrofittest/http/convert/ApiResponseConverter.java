package com.yu.retrofittest.http.convert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.yu.retrofittest.rx.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 对返回的json预处理,根据code判断业务是否为成功
 *
 * @author yu
 *         Create on 16/9/30.
 */
final class ApiResponseConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final boolean flag;

    ApiResponseConverter(Gson gson, TypeAdapter<T> adapter, boolean flag) {
        this.gson = gson;
        this.adapter = adapter;
        this.flag = flag;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String jsonStr = value.string();
            JSONObject jsonObject = new JSONObject(jsonStr);

            int code = jsonObject.optInt("code");
            if (code != 200) {
                // 抛出异常,直接走rxjava的onError回调
                throw new ApiException(code, jsonObject.optString("message", ""));
            } else {
                // 如果是三段的json,取出data字符串,需要配置键值,默认data
                if (flag) {
                    jsonStr = jsonObject.optString("data");
                    if ("".equals(jsonStr)) jsonStr = "{}";
                }
                InputStream inputStream = new ByteArrayInputStream(jsonStr.getBytes());
                Reader reader = new InputStreamReader(inputStream, "utf-8");
                JsonReader jsonReader = gson.newJsonReader(reader);
                return adapter.read(jsonReader);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } finally {
            value.close();
        }
    }
}
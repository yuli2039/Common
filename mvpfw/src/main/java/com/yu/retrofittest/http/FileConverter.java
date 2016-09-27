package com.yu.retrofittest.http;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 自定义converter，以二进制的形式上传文件
 *
 * @author yu
 *         Create on 16/9/8.
 */
public class FileConverter implements Converter<File, RequestBody> {

    @Override
    public RequestBody convert(File value) throws IOException {
        return RequestBody.create(MediaType.parse(guessMimeType(value.getName())), value);
    }

    private String guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        Log.e("FileConverter_", "MediaType = " + contentTypeFor);
        return contentTypeFor;
    }

    public static class Factory extends Converter.Factory {

        private static final Factory INSTANCE = new Factory();

        public static Factory create() {
            return INSTANCE;
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter
                (Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            if (type == File.class) {
                Log.e("FileConverter_", "post file as bytes");
                return new FileConverter();
            }
            return null;
        }
    }
}

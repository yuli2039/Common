package com.yu.falcon.builder;

import com.yu.falcon.callback.Callback;
import com.yu.falcon.Falcon;
import com.yu.falcon.core.CountingRequestBody;
import com.yu.falcon.core.Paramsable;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author yu
 *         Create on 16/7/19.
 */
public class PostFormBuilder extends AbsRequestBuilder<PostFormBuilder> implements Paramsable {

    private List<FileInput> files = new ArrayList<>();

    public PostFormBuilder addFile(String name, String filename, File file) {
        files.add(new FileInput(name, filename, file));
        return this;
    }

    public PostFormBuilder files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(key, filename, files.get(filename)));
        }
        return this;
    }

    @Override
    public AbsRequestBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public AbsRequestBuilder addParams(String key, String val) {
        if (params == null)
            params = new LinkedHashMap<>();
        params.put(key, val);
        return this;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (files == null || files.isEmpty()) {
            FormBody.Builder fbuilder = new FormBody.Builder();
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    fbuilder.add(key, params.get(key));
                }
            }
            return fbuilder.build();
        } else {
            MultipartBody.Builder mbuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    mbuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                            RequestBody.create(null, params.get(key)));
                }
            }
            FileInput fileInput;
            MediaType type;
            RequestBody fileBody;
            for (int i = 0; i < files.size(); i++) {
                fileInput = files.get(i);
                type = MediaType.parse(guessMimeType(fileInput.filename));
                fileBody = RequestBody.create(type, fileInput.file);
                mbuilder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
            }
            return mbuilder.build();
        }
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody body, final Callback callback) {
        if (callback == null) return body;
        return new CountingRequestBody(body, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength) {

                Falcon.getInstance().getDelivery().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.inProgress(bytesWritten * 1.0f / contentLength, contentLength);
                    }
                });

            }
        });
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    /**
     * 推测file body的type
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }
}

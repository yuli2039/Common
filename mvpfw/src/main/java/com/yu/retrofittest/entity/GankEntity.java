package com.yu.retrofittest.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author yu
 *         Create on 16/10/26.
 */
public class GankEntity {

    /**
     * _id : 580ff3fd421aa913769745bb
     * createdAt : 2016-10-26T08:08:29.661Z
     * desc : 高仿微信视差手势滑动返回，无需设置透明主题
     * images : ["http://img.gank.io/2873a863-09ce-4581-91bc-5707d9b3a993"]
     * publishedAt : 2016-10-26T11:28:10.759Z
     * source : web
     * type : Android
     * url : https://github.com/oubowu/SlideBack
     * used : true
     * who : Ou Bowu
     */

    @SerializedName("_id")
    private String id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;
    private List<String> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}

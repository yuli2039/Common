package com.yu.retrofittest;

import java.util.List;

/**
 * @author yu
 *         Create on 16/7/26.
 */
public class GankEn {

    /**
     * count : 2
     * error : false
     * results : [{"desc":"还在用ListView？","ganhuo_id":"57334c9d67765903fb61c418","publishedAt":"2016-05-12T12:04:43.857000","readability":"xx","type":"Android","url":"http://www.jianshu.com/p/a92955be0a3e","who":"陈宇明"},{"desc":"listview的折叠效果","ganhuo_id":"56cc6d1d421aa95caa7076fa","publishedAt":"2015-07-17T03:43:22.395000","readability":"cc","type":"Android","url":"https://github.com/dodola/ListItemFold","who":"Jason"}]
     */

    private int count;
    private boolean error;
    /**
     * desc : 还在用ListView？
     * ganhuo_id : 57334c9d67765903fb61c418
     * publishedAt : 2016-05-12T12:04:43.857000
     * readability : xx
     * type : Android
     * url : http://www.jianshu.com/p/a92955be0a3e
     * who : 陈宇明
     */

    private List<ResultsEntity> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public static class ResultsEntity {
        private String desc;
        private String ganhuo_id;
        private String publishedAt;
        private String readability;
        private String type;
        private String url;
        private String who;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getGanhuo_id() {
            return ganhuo_id;
        }

        public void setGanhuo_id(String ganhuo_id) {
            this.ganhuo_id = ganhuo_id;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getReadability() {
            return readability;
        }

        public void setReadability(String readability) {
            this.readability = readability;
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

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }
    }
}

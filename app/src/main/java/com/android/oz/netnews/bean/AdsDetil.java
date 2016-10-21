package com.android.oz.netnews.bean;

/**
 * Created by jonesleborn on 16/8/15.
 * 轮播图专用
 */
public class AdsDetil {
    private String imgsrc;
    private String title;

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AdsDetil{" +
                "imgsrc='" + imgsrc + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}

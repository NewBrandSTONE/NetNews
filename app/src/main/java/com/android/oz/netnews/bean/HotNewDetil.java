package com.android.oz.netnews.bean;

import java.util.List;

/**
 * Created by jonesleborn on 16/8/15.
 */
public class HotNewDetil {
    private String title;
    private String img;
    private String source;
    private int replyCount;
    private List<AdsDetil> ads;
    private String docid;
    // 专题才有的specialID
    private String specialID;

    public String getSpecialID() {
        return specialID;
    }

    public void setSpecialID(String specialID) {
        this.specialID = specialID;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public List<AdsDetil> getAds() {
        return ads;
    }

    public void setAds(List<AdsDetil> ads) {
        this.ads = ads;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

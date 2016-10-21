package com.android.oz.netnews.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jonesleborn on 16/8/11.
 */
public class Ads implements Serializable {
    private List<Adert> ads;
    private String error;
    private int next_req;
    private int result;

    public Ads(List<Adert> aderts, String error, int next_req, int result) {
        this.ads = aderts;
        this.error = error;
        this.next_req = next_req;
        this.result = result;
    }

    public List<Adert> getAds() {
        return ads;
    }

    public void setAds(List<Adert> ads) {
        this.ads = ads;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getNext_req() {
        return next_req;
    }

    public void setNext_req(int next_req) {
        this.next_req = next_req;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}

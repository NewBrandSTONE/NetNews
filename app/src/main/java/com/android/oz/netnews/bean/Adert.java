package com.android.oz.netnews.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jonesleborn on 16/8/11.
 */
public class Adert implements Serializable {
    private LinksParams action_params;
    private List<String> res_url;

    public Adert(LinksParams params, List<String> restUrl) {
        this.action_params = params;
        this.res_url = restUrl;
    }

    public LinksParams getAction_params() {
        return action_params;
    }

    public void setAction_params(LinksParams action_params) {
        this.action_params = action_params;
    }

    public List<String> getRes_url() {
        return res_url;
    }

    public void setRes_url(List<String> res_url) {
        this.res_url = res_url;
    }

    @Override
    public String toString() {
        return "Adert{" +
                "action_params=" + action_params +
                ", res_url=" + res_url +
                '}';
    }
}

package com.android.oz.netnews.bean;

import java.io.Serializable;

/**
 * Created by jonesleborn on 16/8/11.
 */
public class LinksParams implements Serializable {
    private String link_url;

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }
}

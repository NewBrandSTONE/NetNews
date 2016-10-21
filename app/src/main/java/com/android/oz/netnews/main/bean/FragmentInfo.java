package com.android.oz.netnews.main.bean;

/**
 * Created by jonesleborn on 16/8/14.
 */
public class FragmentInfo {

    private String title;
    private Class clz;

    public FragmentInfo(String title, Class clz) {
        this.title = title;
        this.clz = clz;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }
}

package com.android.oz.netnews.bean;

import java.io.Serializable;

/**
 * @author O.z Young
 * @date 16/10/22
 * @desc ${CURSOR}
 */

public class DocDetailImg implements Serializable{

    private String ref;
    private String pixel;
    private String alt;
    private String src;
    private String photosetID;

    public DocDetailImg(String ref, String pixel, String alt, String src, String photosetID) {
        this.ref = ref;
        this.pixel = pixel;
        this.alt = alt;
        this.src = src;
        this.photosetID = photosetID;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getPhotosetID() {
        return photosetID;
    }

    public void setPhotosetID(String photosetID) {
        this.photosetID = photosetID;
    }
}

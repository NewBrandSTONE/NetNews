package com.android.oz.netnews.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author O.z Young
 * @date 16/10/22
 * @desc ${CURSOR}
 */

public class DocDetail implements Serializable {
    private String body;
    private int replyCount;
    private List<DocDetailImg> img;
    private String title;
    private String source;
    private String ptime;

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "DocDetail{" +
                "body='" + body + '\'' +
                ", replyCount=" + replyCount +
                ", img=" + img +
                '}';
    }

    public DocDetail(String body, int replyCount, List<DocDetailImg> img) {
        this.body = body;
        this.replyCount = replyCount;
        this.img = img;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public List<DocDetailImg> getImg() {
        return img;
    }

    public void setImg(List<DocDetailImg> img) {
        this.img = img;
    }
}

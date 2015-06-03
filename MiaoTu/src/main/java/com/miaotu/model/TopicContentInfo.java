package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jayden on 2015/6/3.
 */
public class TopicContentInfo {
    @JsonProperty("Sid")
    private String sid;
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("HeadUrl")
    private String headurl;
    @JsonProperty("PicUrl")
    private String picurl;
    @JsonProperty("Content")
    private String content;
    @JsonProperty("Nickname")
    private String nickname;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

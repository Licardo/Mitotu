package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by ying on 2015/6/3.
 */
public class RemindLikeTogetherInfo implements Serializable{
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Nickname")
    private String nickname;
    @JsonProperty("HeadUrl")
    private String headUrl;
    @JsonProperty("Yid")
    private String yid;
    @JsonProperty("PicUrl")
    private String picUrl;
    @JsonProperty("Content")
    private String content;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getYid() {
        return yid;
    }

    public void setYid(String yid) {
        this.yid = yid;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

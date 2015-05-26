package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hao on 2015/3/6.
 */
public class TopicComment implements Serializable {
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Nickname")
    private String nickname;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Content")
    private String content;
    @JsonProperty("Srid")
    private String srid;
    @JsonProperty("HeadUrl")
    private String headurl;

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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSrid() {
        return srid;
    }

    public void setSrid(String srid) {
        this.srid = srid;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }
}

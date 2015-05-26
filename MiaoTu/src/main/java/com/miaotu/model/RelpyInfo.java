package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class RelpyInfo implements Serializable {
    /**
     *
     */
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Nickname")
    private String Nickname;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Srid")
    private String Srid;
    @JsonProperty("Content")
    private String Content;
    @JsonProperty("HeadUrl")
    private String hearurl;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSrid() {
        return Srid;
    }

    public void setSrid(String srid) {
        Srid = srid;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getHearurl() {
        return hearurl;
    }

    public void setHearurl(String hearurl) {
        this.hearurl = hearurl;
    }
}

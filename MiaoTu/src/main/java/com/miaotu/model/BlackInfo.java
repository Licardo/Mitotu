package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jayden on 2015/5/29.
 */
public class BlackInfo {
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Nickname")
    private String nickname;
    @JsonProperty("HeadUrl")
    private String headurl;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("IsLiked")
    private String isliked;
    @JsonProperty("State")
    private String state;

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

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIsliked() {
        return isliked;
    }

    public void setIsliked(String isliked) {
        this.isliked = isliked;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

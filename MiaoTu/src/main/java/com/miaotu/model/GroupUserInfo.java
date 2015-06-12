package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jayden on 2015/6/12.
 */
public class GroupUserInfo {

    @JsonProperty("Nickname")
    private String nickname;
    @JsonProperty("HeadUrl")
    private String headurl;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("IsOwner")
    private String isowner;
    @JsonProperty("IsAdmin")
    private String isadmin;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIsowner() {
        return isowner;
    }

    public void setIsowner(String isowner) {
        this.isowner = isowner;
    }

    public String getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(String isadmin) {
        this.isadmin = isadmin;
    }
}

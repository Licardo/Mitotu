package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by hao on 2015/5/26.
 */
public class LikeInfo {
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Nickname ")
    private String nickname;
    @JsonProperty("HeadUrl")
    private String headurl;
    @JsonProperty("IsLiked")
    private String isliked;

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

    public String getIsliked() {
        return isliked;
    }

    public void setIsliked(String isliked) {
        this.isliked = isliked;
    }
}

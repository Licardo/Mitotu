package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ying on 2015/3/6.
 */
public class Topic implements Serializable {
    @JsonProperty("Sid")
    private String sid;
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Content")
    private String content;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("StateReplyCount")
    private String statereplycount;
    @JsonProperty("StateLikeCount")
    private String statelikecount;
    @JsonProperty("StateClickCount")
    private String stateclickcount;
    @JsonProperty("AtUser")
    private String atuser;
    @JsonProperty("Latitude")
    private String latitude;
    @JsonProperty("Longitude")
    private String longitude;
    @JsonProperty("Distance")
    private String distance;
    @JsonProperty("Aid")
    private String aid;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Nickname")
    private String nickname;
    @JsonProperty("HeadUrl")
    private String head_url;
    @JsonProperty("Age")
    private String age;
    @JsonProperty("Gender")
    private String Gender;
    @JsonProperty("IsLike")
    private String islike;
    @JsonProperty("PicList")
    private List<PhotoInfo> piclist;
    @JsonProperty("LikeList")
    private List<LikeInfo> likelist;
    @JsonProperty("ReplyList")
    private List<RelpyInfo> replylist;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatereplycount() {
        return statereplycount;
    }

    public void setStatereplycount(String statereplycount) {
        this.statereplycount = statereplycount;
    }

    public String getStatelikecount() {
        return statelikecount;
    }

    public void setStatelikecount(String statelikecount) {
        this.statelikecount = statelikecount;
    }

    public String getStateclickcount() {
        return stateclickcount;
    }

    public void setStateclickcount(String stateclickcount) {
        this.stateclickcount = stateclickcount;
    }

    public String getAtuser() {
        return atuser;
    }

    public void setAtuser(String atuser) {
        this.atuser = atuser;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }

    public List<PhotoInfo> getPiclist() {
        return piclist;
    }

    public void setPiclist(List<PhotoInfo> piclist) {
        this.piclist = piclist;
    }

    public List<LikeInfo> getLikelist() {
        return likelist;
    }

    public void setLikelist(List<LikeInfo> likelist) {
        this.likelist = likelist;
    }

    public List<RelpyInfo> getReplylist() {
        return replylist;
    }

    public void setReplylist(List<RelpyInfo> replylist) {
        this.replylist = replylist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

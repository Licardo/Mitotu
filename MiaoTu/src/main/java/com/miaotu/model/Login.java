package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ying on 2015/5/21.
 */
public class Login {
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Token")
    private String token;
    @JsonProperty("Nickname")
    private String name;
    @JsonProperty("HeadUrl")
    private String headPhoto;
    @JsonProperty("Age")
    private String age;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("Work")
    private String job;
    @JsonProperty("LikedCount")
    private String fanscount;
    @JsonProperty("LikeCount")
    private String followcount;
    @JsonProperty("WxUnionid")
    private String wxunionid;
    @JsonProperty("QqOpenid")
    private String qqopenid;
    @JsonProperty("SinaUid")
    private String sinauid;
    @JsonProperty("LuckyMoney")
    private String luckymoney;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getFanscount() {
        return fanscount;
    }

    public void setFanscount(String fanscount) {
        this.fanscount = fanscount;
    }

    public String getFollowcount() {
        return followcount;
    }

    public void setFollowcount(String followcount) {
        this.followcount = followcount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWxunionid() {
        return wxunionid;
    }

    public void setWxunionid(String wxunionid) {
        this.wxunionid = wxunionid;
    }

    public String getQqopenid() {
        return qqopenid;
    }

    public void setQqopenid(String qqopenid) {
        this.qqopenid = qqopenid;
    }

    public String getSinauid() {
        return sinauid;
    }

    public void setSinauid(String sinauid) {
        this.sinauid = sinauid;
    }

    public String getLuckymoney() {
        return luckymoney;
    }

    public void setLuckymoney(String luckymoney) {
        this.luckymoney = luckymoney;
    }
}

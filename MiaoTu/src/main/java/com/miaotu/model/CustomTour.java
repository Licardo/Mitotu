package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ying on 2015/5/25.
 */
public class CustomTour {
    @JsonProperty("Aid")
    private String id;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Destination")
    private String destination;
    @JsonProperty("Summary")
    private String summary;
    @JsonProperty("MtPrice")
    private String MtPrice;
    @JsonProperty("StartDate")
    private String startDate;
    @JsonProperty("EndDate")
    private String endDate;
    @JsonProperty("EndTime")
    private String endTime;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Duration")
    private String duration;
    @JsonProperty("AreaTags")
    private String areaTags;
    @JsonProperty("ThemeTags")
    private String themeTags;
    @JsonProperty("FromCity")
    private String fromCity;
    @JsonProperty("Tags")
    private String tags;
    @JsonProperty("ReleaseTime")
    private String releaseTime;
    @JsonProperty("ActivityLikeCount")
    private String likeCount;
    @JsonProperty("ActivityJoinCount")
    private String joinCount;
    @JsonProperty("ActivityReplyCount")
    private String replyCount;
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Cid")
    private String cid;
    @JsonProperty("PicUrl")
    private String picUrl;
    @JsonProperty("Nickname")
    private String nickname;
    @JsonProperty("HeadUrl")
    private String headUrl;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("UserTags")
    private String userTags;
    @JsonProperty("IsLike")
    private boolean isLike;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMtPrice() {
        return MtPrice;
    }

    public void setMtPrice(String mtPrice) {
        MtPrice = mtPrice;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAreaTags() {
        return areaTags;
    }

    public void setAreaTags(String areaTags) {
        this.areaTags = areaTags;
    }

    public String getThemeTags() {
        return themeTags;
    }

    public void setThemeTags(String themeTags) {
        this.themeTags = themeTags;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(String joinCount) {
        this.joinCount = joinCount;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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

    public String getUserTags() {
        return userTags;
    }

    public void setUserTags(String userTags) {
        this.userTags = userTags;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }
}

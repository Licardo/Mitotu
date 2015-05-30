package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jayden on 2015/5/30.
 */
public class CustomTourInfo {
    @JsonProperty("Aid")
    private String aid;
    @JsonProperty("Title")
    private String Title;
    @JsonProperty("Destination")
    private String destination;
    @JsonProperty("Summary")
    private String summary;
    @JsonProperty("PubPrice")
    private String pubprice;
    @JsonProperty("MtPrice")
    private String mtprice;
    @JsonProperty("StartDate")
    private String startdate;
    @JsonProperty("EndDate")
    private String enddate;
    @JsonProperty("EndTime")
    private String endtime;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Duration")
    private String duration;
    @JsonProperty("AreaTags")
    private String areatags;
    @JsonProperty("ThemeTags")
    private String themetags;
    @JsonProperty("FromCity")
    private String fromcity;
    @JsonProperty("Tags")
    private String tags;
    @JsonProperty("ReleaseTime")
    private String releasetime;
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Cid")
    private String cid;
    @JsonProperty("PicUrl")
    private String picurl;
    @JsonProperty("Nickname")
    private String nickname;
    @JsonProperty("HeadUrl")
    private String headurl;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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

    public String getPubprice() {
        return pubprice;
    }

    public void setPubprice(String pubprice) {
        this.pubprice = pubprice;
    }

    public String getMtprice() {
        return mtprice;
    }

    public void setMtprice(String mtprice) {
        this.mtprice = mtprice;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
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

    public String getAreatags() {
        return areatags;
    }

    public void setAreatags(String areatags) {
        this.areatags = areatags;
    }

    public String getThemetags() {
        return themetags;
    }

    public void setThemetags(String themetags) {
        this.themetags = themetags;
    }

    public String getFromcity() {
        return fromcity;
    }

    public void setFromcity(String fromcity) {
        this.fromcity = fromcity;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getReleasetime() {
        return releasetime;
    }

    public void setReleasetime(String releasetime) {
        this.releasetime = releasetime;
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

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
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
}

package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class JoinOrCollect implements Serializable{
	private static final long serialVersionUID = 1L;
	@JsonProperty("activity_id")
	private String activityId;
	@JsonProperty("title")
	private String title;
	@JsonProperty("short_title")
	private String shortTitle;
    @JsonProperty("start_time")
	private String startTime;
    @JsonProperty("end_time")
    private String endTime;
    @JsonProperty("theme_tags")
    private String theme;
    @JsonProperty("activity_status")
    private String status;
    @JsonProperty("mt_price")
    private String price;
    @JsonProperty("tags")
    private String tag;
    @JsonProperty("area_tags")
    private String areaTag;
    @JsonProperty("from_city")
    private String from;
    @JsonProperty("address_plan")
    private String to;
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("img")
    private PhotoInfo photoInfo;
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAreaTag() {
        return areaTag;
    }

    public void setAreaTag(String areaTag) {
        this.areaTag = areaTag;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public PhotoInfo getPhotoInfo() {
        return photoInfo;
    }

    public void setPhotoInfo(PhotoInfo photoInfo) {
        this.photoInfo = photoInfo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

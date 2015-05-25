package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Movement implements Serializable{
	@JsonProperty("id")
	private String id;
	@JsonProperty("short_title")
	private String name;
	@JsonProperty("title")
	private String longName;
	@JsonProperty("mt_price")
	private String price;
	@JsonProperty("start_time")
	private String startDate;
	@JsonProperty("end_time")
	private String endDate;
	@JsonProperty("end_date")
	private String signEndDate;
	@JsonProperty("collect_count")
	private String interestedCounts;
	@JsonProperty("pic_url")
	private String briefPicUrl;
	@JsonProperty("activity_status")
	private String status;
	@JsonProperty("join_count")
	private String count;
	@JsonProperty("is_collect")
	private String isCollected;
    @JsonProperty("theme_tags")
	private String theme;
    @JsonProperty("area_tags")
    private String areaTag;
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("img")
    private PhotoInfo photoInfo;
    @JsonProperty("tags")
    private String tag;
    @JsonProperty("from_city")
    private String from;
    @JsonProperty("address_plan")
    private String to;
    @JsonProperty("sponsor")
    private Sponsor sponsor;
    @JsonProperty("activity_id")
    private String activityId; //发布的活动返回的是activity_id
    @JsonProperty("release_time")
    private String releaseTime;
	@JsonIgnore
	private String isSelected;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public String getInterestedCounts() {
		return interestedCounts;
	}

	public void setInterestedCounts(String interestedCounts) {
		this.interestedCounts = interestedCounts;
	}

	public String getBriefPicUrl() {
		return briefPicUrl;
	}

	public void setBriefPicUrl(String briefPicUrl) {
		this.briefPicUrl = briefPicUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getSignEndDate() {
		return signEndDate;
	}

	public void setSignEndDate(String signEndDate) {
		this.signEndDate = signEndDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getIsCollected() {
		return isCollected;
	}

	public void setIsCollected(String isCollected) {
		this.isCollected = isCollected;
	}

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

	public Sponsor getSponsor() {
		return sponsor;
	}

	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }
}

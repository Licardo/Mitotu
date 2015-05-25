package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class MemberInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("avatar")
	private PhotoInfo avatar;
    @JsonProperty("user_id")
	private String userId;
	@JsonProperty("sex")
	private String gender;
	@JsonProperty("age")
	private String age;
	@JsonProperty("nickname")
	private String nickName;
	@JsonProperty("city")
	private String city;
	@JsonProperty("birthday")
	private String birthday;
	@JsonProperty("time")
	private String visitTime;
	@JsonProperty("collect_count")
	private String count;
	@JsonProperty("online")
	private Boolean isOnline;
    @JsonProperty("like_to_like")
	private Boolean isLikeEach;
    private String isSelected;
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Boolean getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public PhotoInfo getAvatar() {
        return avatar;
    }

    public void setAvatar(PhotoInfo avatar) {
        this.avatar = avatar;
    }
	public String getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}

    public Boolean getIsLikeEach() {
        return isLikeEach;
    }

    public void setIsLikeEach(Boolean isLikeEach) {
        this.isLikeEach = isLikeEach;
    }
}

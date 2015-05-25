package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;


public class Member implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("sex")
	private String gender;
	@JsonIgnore
	private String isSelected;
	@JsonProperty("age")
	private String age;
	@JsonProperty("nickname")
	private String nickName;
	@JsonProperty("city")
	private String city;
	@JsonProperty("birthday")
	private String birthday;
	@JsonProperty("collect_count")
	private String collect;
	@JsonProperty("join_act")
	private String joinAct;
	@JsonProperty("like_to_like")
	private String isLikeEachOther;
	@JsonProperty("liked")
	private String liked;
	@JsonProperty("education")
	private String education;
	@JsonProperty("about_me")
	private String about_me;
	@JsonProperty("join_count")
	private String joinCount;
    @JsonProperty("is_owner")
	private String isOwner;
	@JsonProperty("online")
	private Boolean online;
	@JsonProperty("avatar")
	private PhotoInfo photo;
    @JsonProperty("photo")
	private PhotoInfo waterPhoto;
	@JsonProperty("tags")
	private List<String> tags;
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
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}
	public String getCollect() {
		return collect;
	}
	public void setCollect(String collect) {
		this.collect = collect;
	}
	public String getJoinAct() {
		return joinAct;
	}
	public void setJoinAct(String joinAct) {
		this.joinAct = joinAct;
	}
	public String getIsLikeEachOther() {
		return isLikeEachOther;
	}
	public void setIsLikeEachOther(String isLikeEachOther) {
		this.isLikeEachOther = isLikeEachOther;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getAbout_me() {
		return about_me;
	}
	public void setAbout_me(String about_me) {
		this.about_me = about_me;
	}
	public String getLiked() {
		return liked;
	}
	public void setLiked(String liked) {
		this.liked = liked;
	}
	public PhotoInfo getPhoto() {
		return photo;
	}
	public void setPhoto(PhotoInfo photo) {
		this.photo = photo;
	}
	public Boolean getOnline() {
		return online;
	}
	public void setOnline(Boolean online) {
		this.online = online;
	}
	public String getJoinCount() {
		return joinCount;
	}
	public void setJoinCount(String joinCount) {
		this.joinCount = joinCount;
	}

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public PhotoInfo getWaterPhoto() {
        return waterPhoto;
    }

    public void setWaterPhoto(PhotoInfo waterPhoto) {
        this.waterPhoto = waterPhoto;
    }
}

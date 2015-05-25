package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


/**
 * @author Jayden
 *
 */
public class Sponsor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("nickname")
	private String nickname;
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("type")
	private String type;
	@JsonProperty("avatar")
	private PhotoInfo avatar;
    @JsonProperty("content")
	private String content;
    @JsonProperty("img")
	private PhotoInfo img;
    @JsonProperty("activity_count")
	private String activityCount;
    @JsonProperty("activity")
    private Movement movement;
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public PhotoInfo getAvatar() {
		return avatar;
	}
	public void setAvatar(PhotoInfo avatar) {
		this.avatar = avatar;
	}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PhotoInfo getImg() {
        return img;
    }

    public void setImg(PhotoInfo img) {
        this.img = img;
    }

    public String getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(String activityCount) {
        this.activityCount = activityCount;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }
}

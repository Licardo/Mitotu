package com.miaotu.jpush;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class LikeMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private int messageId;
	@JsonProperty("Uid")
	private String uid;
	@JsonProperty("HeadUrl")
	private String headUrl;
	@JsonProperty("Age")
	private String age;
	@JsonProperty("Gender")
	private String gender;
	@JsonProperty("Nickname")
	private String nickname;
	@JsonProperty("Content")
	private String content;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}

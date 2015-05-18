package com.miaotu.jpush;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class SystemMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private int messageId;
	@JsonIgnore
	private final ThreadLocal<String> messageStatus = new ThreadLocal<>();
	private String messageType;
	@JsonProperty("collect_count")
	private String userInterestCount;
	@JsonProperty("sex")
	private String userGender;
	@JsonProperty("age")
	private String userAge;
	@JsonProperty("nickname")
	private String userName;
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("msg")
	private String messageContent;
	@JsonProperty("time")
	private String messageDate;
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getMessageStatus() {
		return messageStatus.get();
	}
	public void setMessageStatus(String messageStatus) {
		this.messageStatus.set(messageStatus);
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getUserInterestCount() {
		return userInterestCount;
	}
	public void setUserInterestCount(String userInterestCount) {
		this.userInterestCount = userInterestCount;
	}
	public String getUserGender() {
		return userGender;
	}
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	public String getUserAge() {
		return userAge;
	}
	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getMessageDate() {
		return messageDate;
	}
	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}
	
}

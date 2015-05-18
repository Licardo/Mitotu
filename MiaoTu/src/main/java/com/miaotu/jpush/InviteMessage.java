package com.miaotu.jpush;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class InviteMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore
private int inviteId;
	@JsonProperty("status")
private String inviteStatus;
	@JsonProperty("msg")
private String inviteContent;
	@JsonProperty("end_date")
private  String movementEndDate;
	@JsonProperty("from_city")
private  String movementCity;
	@JsonProperty("short_title")
private  String movementName;
	@JsonProperty("activity_id")
private  String movementId;
	@JsonProperty("user_id")
private  String userId;
	@JsonProperty("nickname")
private  String userName;
	@JsonProperty("age")
private  String userAge;
	@JsonProperty("sex")
	private  String userGender;
	@JsonProperty("time")
private  String messageDate;
	@JsonIgnore
	private  String messageStatus;
public int getInviteId() {
	return inviteId;
}
public void setInviteId(int inviteId) {
	this.inviteId = inviteId;
}
public String getInviteStatus() {
	return inviteStatus;
}
public void setInviteStatus(String inviteStatus) {
	this.inviteStatus = inviteStatus;
}
public String getInviteContent() {
	return inviteContent;
}
public void setInviteContent(String inviteContent) {
	this.inviteContent = inviteContent;
}
public String getMovementEndDate() {
	return movementEndDate;
}
public void setMovementEndDate(String movementEndDate) {
	this.movementEndDate = movementEndDate;
}
public String getMovementCity() {
	return movementCity;
}
public void setMovementCity(String movementCity) {
	this.movementCity = movementCity;
}
public String getMovementName() {
	return movementName;
}
public void setMovementName(String movementName) {
	this.movementName = movementName;
}
public String getMovementId() {
	return movementId;
}
public void setMovementId(String movementId) {
	this.movementId = movementId;
}
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getUserAge() {
	return userAge;
}
public void setUserAge(String userAge) {
	this.userAge = userAge;
}
public String getMessageDate() {
	return messageDate;
}
public void setMessageDate(String messageDate) {
	this.messageDate = messageDate;
}
public String getMessageStatus() {
	return messageStatus;
}
public void setMessageStatus(String messageStatus) {
	this.messageStatus = messageStatus;
}
public String getUserGender() {
	return userGender;
}
public void setUserGender(String userGender) {
	this.userGender = userGender;
}

}

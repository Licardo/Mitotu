package com.miaotu.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.User;

/**
 * 
 * @author zhangying
 *
 */
public class UserInfoResult extends BaseResult{
	@JsonProperty("items")
	private User user;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}

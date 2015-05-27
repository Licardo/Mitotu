package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class Money implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("money")
	private String money;
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}

}

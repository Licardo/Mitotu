package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author zhangying
 *
 */
public class BaseResult {
	@JsonIgnore
	public static final int SUCCESS = 0;
	@JsonProperty("Err")
	private int code;//返回状态信息 0成功
	@JsonProperty("Msg")
	private String msg;//附加信息
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}

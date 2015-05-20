package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author zhangying
 *
 */
public class LoginResult extends BaseResult{
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Token")
    private String token;
    @JsonProperty("Nickname")
    private String name;
    @JsonProperty("HeadUrl")
    private String headPhoto;
    @JsonProperty("Age")
    private String age;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("Work")
    private String job;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}

package com.miaotu.form;

import com.miaotu.annotation.FormProperty;

import java.io.Serializable;

/**
 * Created by ying on 2015/5/20.
 */
public class PublishTogether implements Serializable{
    @FormProperty("token")
    private String token;
    @FormProperty("destination")
    private String desCity;
    @FormProperty("from")
    private String originCity;
    @FormProperty("from_mark")
    private String originLocation;
    @FormProperty("start_date")
    private String startDate;
    @FormProperty("end_date")
    private String endDate;
    @FormProperty("end_time")
    private String endTime;
    @FormProperty("require")
    private String requirement;
    @FormProperty("number")
    private String number;
    @FormProperty("money_type")
    private String fee;
    @FormProperty("latitude")
    private String latitude;
    @FormProperty("longitude")
    private String longitude;
    @FormProperty("remark")
    private String remark;
    @FormProperty("tags")
    private String tags;
    @FormProperty("img")
    private String img;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDesCity() {
        return desCity;
    }

    public void setDesCity(String desCity) {
        this.desCity = desCity;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

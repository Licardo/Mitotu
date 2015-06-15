package com.miaotu.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.annotation.FormProperty;

import java.io.Serializable;

/**
 * Created by ying on 2015/6/15.
 */
public class PublishCustomForm implements Serializable{
    @FormProperty("want_go")
    private String desCity;
    @FormProperty("from")
    private String originCity;
    @FormProperty("from_mark")
    private String originLocation;
    @FormProperty("start_date")
    private String startDate;
    @FormProperty("end_date")
    private String endDate;
    @FormProperty("phone")
    private String phone;
    @FormProperty("id_card")
    private String idCard;
    @FormProperty("id_card_pic")
    private String idPic;
    @FormProperty("is_driver")
    private String isDriver;
    @FormProperty("is_freepath")
    private String isFreepath;
    @FormProperty("is_many_tourist")
    private String isManyTourist;
    @FormProperty("is_outbound")
    private String isOutbound;
    @FormProperty("is_tourist")
    private String isTourist;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdPic() {
        return idPic;
    }

    public void setIdPic(String idPic) {
        this.idPic = idPic;
    }

    public String getIsDriver() {
        return isDriver;
    }

    public void setIsDriver(String isDriver) {
        this.isDriver = isDriver;
    }

    public String getIsFreepath() {
        return isFreepath;
    }

    public void setIsFreepath(String isFreepath) {
        this.isFreepath = isFreepath;
    }

    public String getIsManyTourist() {
        return isManyTourist;
    }

    public void setIsManyTourist(String isManyTourist) {
        this.isManyTourist = isManyTourist;
    }

    public String getIsOutbound() {
        return isOutbound;
    }

    public void setIsOutbound(String isOutbound) {
        this.isOutbound = isOutbound;
    }

    public String getIsTourist() {
        return isTourist;
    }

    public void setIsTourist(String isTourist) {
        this.isTourist = isTourist;
    }
}

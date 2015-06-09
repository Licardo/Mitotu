package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by hao on 2015/5/26.
 */
public class EveryDayInfo implements Serializable{
    @JsonProperty("PicUrl")
    private String picUrl;
    @JsonProperty("Lunar")
    private String date1;
    @JsonProperty("Date")
    private String date2;
    @JsonProperty("Dpid")
    private String dpid;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getDpid() {
        return dpid;
    }

    public void setDpid(String dpid) {
        this.dpid = dpid;
    }
}

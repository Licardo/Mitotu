package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ying on 2015/5/25.
 */
public class Banner {
    @JsonProperty("Bid")
    private String bid;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("PicUrl")
    private String picUrl;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Start_date")
    private String startDate;
    @JsonProperty("End_date")
    private String endDate;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Mark")
    private String mark;
    @JsonProperty("Extend")
    private String extend;
    @JsonProperty("Sort")
    private String sort;
    @JsonProperty("Type")
    private String type;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

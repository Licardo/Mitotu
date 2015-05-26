package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class PhotoInfo implements Serializable {
    /**
     *
     */
    @JsonProperty("Spid")
    private String spid;
    @JsonProperty("Sid")
    private String sid;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Url")
    private String url;
    @JsonProperty("Status")
    private String status;

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

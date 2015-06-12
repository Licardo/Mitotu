package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jayden on 2015/6/12.
 */
public class GroupDetailInfo {

    @JsonProperty("Gid")
    private String gid;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Desc")
    private String desc;
    @JsonProperty("Notice")
    private String notice;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Value")
    private String value;
    @JsonProperty("IsOwner")
    private String isowner;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIsowner() {
        return isowner;
    }

    public void setIsowner(String isowner) {
        this.isowner = isowner;
    }
}

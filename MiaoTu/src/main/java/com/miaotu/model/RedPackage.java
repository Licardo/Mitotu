package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class RedPackage implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Money")
    private String money;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Extend")
    private String extend;
    @JsonProperty("ExtendType")
    private String extendtype;
    @JsonProperty("Mark")
    private String mark;
    @JsonProperty("Created")
    private String created;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getExtendtype() {
        return extendtype;
    }

    public void setExtendtype(String extendtype) {
        this.extendtype = extendtype;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}

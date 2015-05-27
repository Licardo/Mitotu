package com.miaotu.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hao on 2015/3/6.
 */
public class TopicMessage implements Serializable {
    @JsonProperty("Smid")
    private String smid;
    @JsonProperty("Sid")
    private String sid;
    @JsonProperty("Nickname")
    private String nickname;
    @JsonProperty("HeadUrl")
    private String headurl;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Remark")
    private String remark;
    @JsonProperty("Content")
    private String content;
    @JsonProperty("PicUrl")
    private String picurl;
    @JsonProperty("Status")
    private String status;

    public String getSmid() {
        return smid;
    }

    public void setSmid(String smid) {
        this.smid = smid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

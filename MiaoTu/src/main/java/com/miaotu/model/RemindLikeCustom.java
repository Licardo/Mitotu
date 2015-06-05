package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ying on 2015/6/3.
 */
public class RemindLikeCustom {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Content")
    private RemindLikeCustomInfo remindLikeCustomInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public RemindLikeCustomInfo getRemindLikeCustomInfo() {
        return remindLikeCustomInfo;
    }

    public void setRemindLikeCustomInfo(RemindLikeCustomInfo remindLikeCustomInfo) {
        this.remindLikeCustomInfo = remindLikeCustomInfo;
    }
}

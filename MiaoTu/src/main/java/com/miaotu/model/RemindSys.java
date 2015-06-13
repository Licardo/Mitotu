package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by ying on 2015/6/3.
 */
public class RemindSys implements Serializable{
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Content")
    private RemindSysContent content;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RemindSysContent getContent() {
        return content;
    }

    public void setContent(RemindSysContent content) {
        this.content = content;
    }
}

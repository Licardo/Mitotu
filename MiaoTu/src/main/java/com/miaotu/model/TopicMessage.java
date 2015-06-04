package com.miaotu.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hao on 2015/3/6.
 */
public class TopicMessage implements Serializable {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Updated")
    private String updated;
    @JsonProperty("Content")
    private TopicContentInfo content;
    @JsonProperty("Deleted")
    private String deleted;
    @JsonProperty("Status")
    private String status;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public TopicContentInfo getContent() {
        return content;
    }

    public void setContent(TopicContentInfo content) {
        this.content = content;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

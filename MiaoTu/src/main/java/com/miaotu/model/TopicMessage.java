package com.miaotu.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ying on 2015/3/6.
 */
public class TopicMessage implements Serializable{
    @JsonProperty("message_id")
    private String messageId;
    @JsonProperty("t_id")
    private String id;
    @JsonProperty("time")
    private String date;
    @JsonProperty("title")
    private String title;
    @JsonProperty("message_status")
    private String status;//0代表未读，1代表已读

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}

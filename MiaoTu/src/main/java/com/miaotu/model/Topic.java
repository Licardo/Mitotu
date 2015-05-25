package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ying on 2015/3/6.
 */
public class Topic implements Serializable{
    @JsonProperty("id")
    private String id;
    @JsonProperty("create_time")
    private String date;
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;
    @JsonProperty("imgs")
    private List<PhotoInfo> pics;
    @JsonProperty("activity_id")
    private String movementId;
    @JsonProperty("extend")
    private String fromId;
    @JsonProperty("activity_title")
    private String movementTitle;
    @JsonProperty("reply_count")
    private String commentCount;
    @JsonProperty("user_id")
    private String uid;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("avatar")
    private PhotoInfo headPhoto;
    @JsonProperty("img")
    private PhotoInfo  singleImg;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<PhotoInfo> getPics() {
        return pics;
    }

    public void setPics(List<PhotoInfo> pics) {
        this.pics = pics;
    }

    public String getMovementId() {
        return movementId;
    }

    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    public String getMovementTitle() {
        return movementTitle;
    }

    public void setMovementTitle(String movementTitle) {
        this.movementTitle = movementTitle;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public PhotoInfo getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(PhotoInfo headPhoto) {
        this.headPhoto = headPhoto;
    }

    public PhotoInfo getSingleImg() {
        return singleImg;
    }

    public void setSingleImg(PhotoInfo singleImg) {
        this.singleImg = singleImg;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
}

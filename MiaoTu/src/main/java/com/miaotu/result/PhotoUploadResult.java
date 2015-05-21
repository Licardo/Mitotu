package com.miaotu.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.PhotoInfo;

import java.util.List;

/**
 * 
 * @author zhanglei
 * 
 *
 */
public class PhotoUploadResult extends BaseResult {
    @JsonProperty("items")
    private List<String>photoList;

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }
}

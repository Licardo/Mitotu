package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.LikeInfo;

import java.util.List;

/**
 * Created by Jayden on 2015/5/30.
 */
public class LikeResult extends BaseResult {
    @JsonProperty("Items")
    private List<LikeInfo> likeInfoList;

    public List<LikeInfo> getLikeInfoList() {
        return likeInfoList;
    }

    public void setLikeInfoList(List<LikeInfo> likeInfoList) {
        this.likeInfoList = likeInfoList;
    }
}

package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.JoinedListInfo;

import java.util.List;

/**
 * Created by Jayden on 2015/6/3.
 */
public class JoinedListResult extends BaseResult{
    @JsonProperty("Items")
    private List<JoinedListInfo> joinedListInfoList;

    public List<JoinedListInfo> getJoinedListInfoList() {
        return joinedListInfoList;
    }

    public void setJoinedListInfoList(List<JoinedListInfo> joinedListInfoList) {
        this.joinedListInfoList = joinedListInfoList;
    }
}

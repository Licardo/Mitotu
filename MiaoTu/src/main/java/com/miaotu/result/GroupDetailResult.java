package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.GroupDetailInfo;

import java.util.List;

/**
 * Created by Jayden on 2015/6/12.
 */
public class GroupDetailResult extends BaseResult {
    @JsonProperty("Items")
    private GroupDetailInfo infolist;

    public GroupDetailInfo getInfolist() {
        return infolist;
    }

    public void setInfolist(GroupDetailInfo infolist) {
        this.infolist = infolist;
    }
}

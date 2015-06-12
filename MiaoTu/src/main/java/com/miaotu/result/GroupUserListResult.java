package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.GroupUserInfo;

import java.util.List;

/**
 * Created by Jayden on 2015/6/12.
 */
public class GroupUserListResult extends BaseResult {

    @JsonProperty("Items")
    private List<GroupUserInfo> groupUserInfoList;

    public List<GroupUserInfo> getGroupUserInfoList() {
        return groupUserInfoList;
    }

    public void setGroupUserInfoList(List<GroupUserInfo> groupUserInfoList) {
        this.groupUserInfoList = groupUserInfoList;
    }
}

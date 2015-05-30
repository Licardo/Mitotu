package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.BlackInfo;

import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class BlackResult extends BaseResult {
    @JsonProperty("Items")
    private List<BlackInfo> blackInfos;

    public List<BlackInfo> getBlackInfos() {
        return blackInfos;
    }

    public void setBlackInfos(List<BlackInfo> blackInfos) {
        this.blackInfos = blackInfos;
    }
}

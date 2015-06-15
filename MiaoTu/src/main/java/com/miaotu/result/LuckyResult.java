package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.LuckyInfo;

/**
 * Created by Jayden on 2015/5/28.
 */
public class LuckyResult extends BaseResult{
    @JsonProperty("Items")
    private LuckyInfo luckyInfo;

    public LuckyInfo getLuckyInfo() {
        return luckyInfo;
    }

    public void setLuckyInfo(LuckyInfo luckyInfo) {
        this.luckyInfo = luckyInfo;
    }
}

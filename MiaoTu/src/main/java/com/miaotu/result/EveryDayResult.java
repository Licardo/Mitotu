package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.EveryDayInfo;
import com.miaotu.model.LikeInfo;

/**
 * Created by Jayden on 2015/5/30.
 */
public class EveryDayResult extends BaseResult {
    @JsonProperty("Items")
    private EveryDayInfo everyDayInfo;

    public EveryDayInfo getEveryDayInfo() {
        return everyDayInfo;
    }

    public void setEveryDayInfo(EveryDayInfo everyDayInfo) {
        this.everyDayInfo = everyDayInfo;
    }
}

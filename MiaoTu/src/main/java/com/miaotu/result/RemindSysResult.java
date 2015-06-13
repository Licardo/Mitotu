package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.RemindLike;
import com.miaotu.model.RemindSys;

import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class RemindSysResult extends BaseResult {
    @JsonProperty("Items")
    private List<RemindSys> remindSyses;

    public List<RemindSys> getRemindSyses() {
        return remindSyses;
    }

    public void setRemindSyses(List<RemindSys> remindSyses) {
        this.remindSyses = remindSyses;
    }
}

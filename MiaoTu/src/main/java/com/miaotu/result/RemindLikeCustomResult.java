package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.RemindLikeCustom;
import com.miaotu.model.RemindLikeTogether;

import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class RemindLikeCustomResult extends BaseResult {
    @JsonProperty("Items")
    private List<RemindLikeCustom> remindLikeCustoms;

    public List<RemindLikeCustom> getRemindLikeCustoms() {
        return remindLikeCustoms;
    }

    public void setRemindLikeCustoms(List<RemindLikeCustom> remindLikeCustoms) {
        this.remindLikeCustoms = remindLikeCustoms;
    }
}

package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.BlackInfo;
import com.miaotu.model.RemindLike;

import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class RemindLikeResult extends BaseResult {
    @JsonProperty("Items")
    private List<RemindLike> remindLikes;

    public List<RemindLike> getRemindLikes() {
        return remindLikes;
    }

    public void setRemindLikes(List<RemindLike> remindLikes) {
        this.remindLikes = remindLikes;
    }
}

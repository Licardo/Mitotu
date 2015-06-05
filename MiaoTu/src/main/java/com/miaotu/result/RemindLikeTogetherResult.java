package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.RemindLike;
import com.miaotu.model.RemindLikeTogether;

import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class RemindLikeTogetherResult extends BaseResult {
    @JsonProperty("Items")
    private List<RemindLikeTogether> remindLikeTogethers;

    public List<RemindLikeTogether> getRemindLikeTogethers() {
        return remindLikeTogethers;
    }

    public void setRemindLikeTogethers(List<RemindLikeTogether> remindLikeTogethers) {
        this.remindLikeTogethers = remindLikeTogethers;
    }
}

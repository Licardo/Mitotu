package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Together;

/**
 * Created by Jayden on 2015/6/16.
 */
public class PublishTogetherResult extends BaseResult{
    @JsonProperty("Items")
    private Together together;

    public Together getTogether() {
        return together;
    }

    public void setTogether(Together together) {
        this.together = together;
    }
}

package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Together;

import java.util.List;

/**
 * 
 * @author zhangying
 *
 */
public class TogetherDetailResult extends BaseResult{
    @JsonProperty("Items")
    private Together together;

    public Together getTogether() {
        return together;
    }

    public void setTogether(Together together) {
        this.together = together;
    }
}

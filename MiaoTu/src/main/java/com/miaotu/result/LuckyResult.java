package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jayden on 2015/5/28.
 */
public class LuckyResult extends BaseResult{
    @JsonProperty("Items")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

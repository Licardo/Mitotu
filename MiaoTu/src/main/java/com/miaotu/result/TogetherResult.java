package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Login;
import com.miaotu.model.Together;

import java.util.List;

/**
 * 
 * @author zhangying
 *
 */
public class TogetherResult extends BaseResult{
    @JsonProperty("Items")
    private List<Together> togetherList;

    public List<Together> getTogetherList() {
        return togetherList;
    }

    public void setTogetherList(List<Together> togetherList) {
        this.togetherList = togetherList;
    }
}

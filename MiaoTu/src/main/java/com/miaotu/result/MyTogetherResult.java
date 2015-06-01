package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Together;

import java.util.List;

/**
 * Created by Jayden on 2015/5/30.
 */
public class MyTogetherResult extends BaseResult {
    @JsonProperty("Items")
    private List<Together> dateTourInfoList;

    public List<Together> getDateTourInfoList() {
        return dateTourInfoList;
    }

    public void setDateTourInfoList(List<Together> dateTourInfoList) {
        this.dateTourInfoList = dateTourInfoList;
    }
}

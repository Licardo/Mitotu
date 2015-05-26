package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by ying on 2015/5/26.
 */
public class SearchTour {
    @JsonProperty("YueyouList")
    private List<Together> togetherList;
    @JsonProperty("ActivityList")
    private List<CustomTour> customTourList;

    public List<Together> getTogetherList() {
        return togetherList;
    }

    public void setTogetherList(List<Together> togetherList) {
        this.togetherList = togetherList;
    }

    public List<CustomTour> getCustomTourList() {
        return customTourList;
    }

    public void setCustomTourList(List<CustomTour> customTourList) {
        this.customTourList = customTourList;
    }
}

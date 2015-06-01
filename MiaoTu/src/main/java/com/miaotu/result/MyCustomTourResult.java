package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.CustomTour;
import com.miaotu.model.CustomTourInfo;

import java.util.List;

/**
 * Created by Jayden on 2015/5/30.
 */
public class MyCustomTourResult extends BaseResult {
    @JsonProperty("Items")
    private List<CustomTour> customTourInfolist;

    public List<CustomTour> getCustomTourInfolist() {
        return customTourInfolist;
    }

    public void setCustomTourInfolist(List<CustomTour> customTourInfolist) {
        this.customTourInfolist = customTourInfolist;
    }
}

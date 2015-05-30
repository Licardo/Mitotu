package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.CustomTourInfo;

import java.util.List;

/**
 * Created by Jayden on 2015/5/30.
 */
public class MyCustomTourResult extends BaseResult {
    @JsonProperty("Items")
    private List<CustomTourInfo> customTourInfolist;

    public List<CustomTourInfo> getCustomTourInfolist() {
        return customTourInfolist;
    }

    public void setCustomTourInfolist(List<CustomTourInfo> customTourInfolist) {
        this.customTourInfolist = customTourInfolist;
    }
}

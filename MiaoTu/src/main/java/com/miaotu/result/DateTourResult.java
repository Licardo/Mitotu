package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.CustomTourInfo;
import com.miaotu.model.DateTourInfo;

import java.util.List;

/**
 * Created by Jayden on 2015/5/30.
 */
public class DateTourResult extends BaseResult {
    @JsonProperty("Items")
    private List<DateTourInfo> dateTourInfoList;

    public List<DateTourInfo> getDateTourInfoList() {
        return dateTourInfoList;
    }

    public void setDateTourInfoList(List<DateTourInfo> dateTourInfoList) {
        this.dateTourInfoList = dateTourInfoList;
    }
}

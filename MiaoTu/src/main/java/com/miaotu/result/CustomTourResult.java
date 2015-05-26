package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Banner;
import com.miaotu.model.CustomTour;
import com.miaotu.model.Together;

import java.util.List;

/**
 * 
 * @author zhangying
 *
 */
public class CustomTourResult extends BaseResult{
    @JsonProperty("Items")
    private List<CustomTour> customTourList;
    @JsonProperty("Banner")
    private List<Banner> bannerList;

    public List<CustomTour> getCustomTourList() {
        return customTourList;
    }

    public void setCustomTourList(List<CustomTour> customTourList) {
        this.customTourList = customTourList;
    }

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }
}

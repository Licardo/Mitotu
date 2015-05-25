package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Banner;
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
    @JsonProperty("Banner")
    private List<Banner> bannerList;

    public List<Together> getTogetherList() {
        return togetherList;
    }

    public void setTogetherList(List<Together> togetherList) {
        this.togetherList = togetherList;
    }

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }
}

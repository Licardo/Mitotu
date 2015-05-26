package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Banner;
import com.miaotu.model.SearchTour;
import com.miaotu.model.Together;

import java.util.List;

/**
 * 
 * @author zhangying
 *
 */
public class SearchTourResult extends BaseResult{
    @JsonProperty("Items")
    private SearchTour searchTour;
}

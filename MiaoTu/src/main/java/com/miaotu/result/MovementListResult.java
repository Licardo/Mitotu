package com.miaotu.result;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.CustomTour;

/**
 * 
 * @author zhangying
 *
 */
public class MovementListResult extends BaseResult{
	@JsonProperty("items")
	private List<CustomTour> results;
	public List<CustomTour> getResults() {
		return results;
	}
	public void setResults(List<CustomTour> results) {
		this.results = results;
	}
	
}

package com.miaotu.result;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Movement;

/**
 * 
 * @author zhangying
 *
 */
public class MovementListResult extends BaseResult{
	@JsonProperty("items")
	private List<Movement> results;
	public List<Movement> getResults() {
		return results;
	}
	public void setResults(List<Movement> results) {
		this.results = results;
	}
	
}

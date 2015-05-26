package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Topic;


import java.util.List;

/**
 * 
 * @author zhangying
 *
 */
public class TopicListResult extends BaseResult{
	@JsonProperty("Items")
	private List<Topic> topics;

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
}

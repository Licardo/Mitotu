package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Topic;


import java.util.List;

/**
 * 
 * @author zhangying
 *
 */
public class TopicResult extends BaseResult{
	@JsonProperty("items")
	private Topic topic;

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}

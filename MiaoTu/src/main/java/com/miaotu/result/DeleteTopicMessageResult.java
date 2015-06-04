package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.TopicMessage;

/**
 * Created by Jayden on 2015/6/4.
 */
public class DeleteTopicMessageResult extends BaseResult {
    @JsonProperty("Items")
    private TopicMessage topicMessage;

    public TopicMessage getTopicMessage() {
        return topicMessage;
    }

    public void setTopicMessage(TopicMessage topicMessage) {
        this.topicMessage = topicMessage;
    }
}

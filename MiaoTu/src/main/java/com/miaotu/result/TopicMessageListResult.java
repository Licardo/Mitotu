package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.TopicComment;
import com.miaotu.model.TopicMessage;


import java.util.List;

/**
 * 
 * @author zhangying
 *
 */
public class TopicMessageListResult extends BaseResult{
	@JsonProperty("Items")
	private List<TopicMessage> messages;

    public List<TopicMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<TopicMessage> messages) {
        this.messages = messages;
    }
}

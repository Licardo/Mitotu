package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Topic;
import com.miaotu.model.TopicComment;


import java.util.List;

/**
 * 
 * @author zhangying
 *
 */
public class TopicCommentsListResult extends BaseResult{
	@JsonProperty("items")
	private List<TopicComment> comment;

    public List<TopicComment> getComment() {
        return comment;
    }

    public void setComment(List<TopicComment> comment) {
        this.comment = comment;
    }
}

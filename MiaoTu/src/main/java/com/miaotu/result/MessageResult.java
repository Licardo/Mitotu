package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Message;

/**
 * Created by Jayden on 2015/5/28.
 */
public class MessageResult extends BaseResult {
    @JsonProperty("Items")
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}

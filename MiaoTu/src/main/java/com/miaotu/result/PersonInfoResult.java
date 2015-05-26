package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.PersonInfo;
import com.miaotu.result.BaseResult;

import java.io.Serializable;

/**
 * Created by hao on 2015/5/20.
 */
public class PersonInfoResult extends BaseResult implements Serializable {
    @JsonProperty("Items")
    private PersonInfo personInfo;

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}

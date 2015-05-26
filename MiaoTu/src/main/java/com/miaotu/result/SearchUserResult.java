package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.PersonInfo;
import com.miaotu.model.SearchTour;

import java.util.List;

/**
 * 
 * @author zhangying
 *
 */
public class SearchUserResult extends BaseResult{
    @JsonProperty("Items")
    private List<PersonInfo> personInfo;

    public List<PersonInfo> getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(List<PersonInfo> personInfo) {
        this.personInfo = personInfo;
    }
}

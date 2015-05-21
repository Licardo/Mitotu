package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Login;

/**
 * 
 * @author zhangying
 *
 */
public class LoginResult extends BaseResult{
    @JsonProperty("Items")
    private Login login;

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}

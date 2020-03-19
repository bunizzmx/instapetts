package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.UserBean;

public class SimpleResponseLogin {
    int code_response;
    UserBean data_user;

    public SimpleResponseLogin() {
    }

    public SimpleResponseLogin(int code_response, UserBean data_user) {
        this.code_response = code_response;
        this.data_user = data_user;
    }

    public SimpleResponseLogin(int code_response) {
        this.code_response = code_response;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }

    public UserBean getData_user() {
        return data_user;
    }

    public void setData_user(UserBean data_user) {
        this.data_user = data_user;
    }
}

package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.SearchUserBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchUsersResponse {
    int code_response;

    @SerializedName("list_users")
    @Expose
    ArrayList<SearchUserBean> list_users;

    public SearchUsersResponse() {
    }

    public SearchUsersResponse(int code_response, ArrayList<SearchUserBean> list_users) {
        this.code_response = code_response;
        this.list_users = list_users;
    }



    public SearchUsersResponse(int code_response) {
        this.code_response = code_response;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }

    public ArrayList<SearchUserBean> getList_users() {
        return list_users;
    }

    public void setList_users(ArrayList<SearchUserBean> list_users) {
        this.list_users = list_users;
    }
}

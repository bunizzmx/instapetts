package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponsePostRecomended {
    @SerializedName("list_posts")
    @Expose
    ArrayList<PostBean> list_posts ;


    @SerializedName("list_users")
    @Expose
    ArrayList<UserBean> list_users;
    int code_response;

    public ResponsePostRecomended() {
    }

    public ResponsePostRecomended(ArrayList<PostBean> list_posts, int code_response) {
        this.list_posts = list_posts;
        this.code_response = code_response;
    }

    public ResponsePostRecomended(ArrayList<PostBean> list_posts, ArrayList<UserBean> list_users, int code_response) {
        this.list_posts = list_posts;
        this.list_users = list_users;
        this.code_response = code_response;
    }

    public ArrayList<UserBean> getList_users() {
        return list_users;
    }

    public void setList_users(ArrayList<UserBean> list_users) {
        this.list_users = list_users;
    }

    public ArrayList<PostBean> getList_posts() {
        return list_posts;
    }

    public void setList_posts(ArrayList<PostBean> list_posts) {
        this.list_posts = list_posts;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }
}

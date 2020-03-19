package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;

import java.util.ArrayList;

public class ResponseProfileUser {

    int code_response;
    UserBean data_user;
    ArrayList<PetBean> petsUser;
    ArrayList<PostBean> postsUser;

    public ResponseProfileUser(UserBean data_user, ArrayList<PetBean> petsUser, ArrayList<PostBean> postsUser) {
        this.data_user = data_user;
        this.petsUser = petsUser;
        this.postsUser = postsUser;
    }

    public ResponseProfileUser() {
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

    public ArrayList<PetBean> getPetsUser() {
        return petsUser;
    }

    public void setPetsUser(ArrayList<PetBean> petsUser) {
        this.petsUser = petsUser;
    }

    public ArrayList<PostBean> getPostsUser() {
        return postsUser;
    }

    public void setPostsUser(ArrayList<PostBean> postsUser) {
        this.postsUser = postsUser;
    }
}

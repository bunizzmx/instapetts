package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.PostBean;

import java.util.ArrayList;

public class ResponsePost {
    ArrayList<PostBean> list_posts ;
    int code_response;

    public ResponsePost(ArrayList<PostBean> list_posts, int code_response) {
        this.list_posts = list_posts;
        this.code_response = code_response;
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

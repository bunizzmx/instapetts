package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;

import java.util.ArrayList;

public class ResponsePost {
    ArrayList<PostBean> list_posts ;
    ArrayList<HistoriesBean> list_stories;
    int code_response;

    public ResponsePost() {
    }

    public ResponsePost(ArrayList<PostBean> list_posts, int code_response) {
        this.list_posts = list_posts;
        this.code_response = code_response;
    }

    public ResponsePost(ArrayList<PostBean> list_posts, ArrayList<HistoriesBean> list_stories, int code_response) {
        this.list_posts = list_posts;
        this.list_stories = list_stories;
        this.code_response = code_response;
    }

    public ArrayList<HistoriesBean> getList_stories() {
        return list_stories;
    }

    public void setList_stories(ArrayList<HistoriesBean> list_stories) {
        this.list_stories = list_stories;
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

package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PlayVideos;
import com.bunizz.instapetts.beans.PostBean;

import java.util.ArrayList;

public class ResponsePlayVideos {
    ArrayList<PlayVideos> list_videos ;

    int code_response;

    public ResponsePlayVideos() {
    }

    public ResponsePlayVideos(ArrayList<PlayVideos> list_posts, int code_response) {
        this.list_videos = list_posts;
        this.code_response = code_response;
    }

    public ArrayList<PlayVideos> getList_videos() {
        return list_videos;
    }

    public void setList_videos(ArrayList<PlayVideos> list_posts) {
        this.list_videos = list_posts;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }
}

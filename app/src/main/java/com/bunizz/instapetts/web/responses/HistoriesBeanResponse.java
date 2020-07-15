package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.HistoriesBean;

import java.util.ArrayList;

public class HistoriesBeanResponse {

    int code_response;
    ArrayList<HistoriesBean> list_stories;

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }

    public ArrayList<HistoriesBean> getList_stories() {
        return list_stories;
    }

    public void setList_stories(ArrayList<HistoriesBean> list_stories) {
        this.list_stories = list_stories;
    }
}

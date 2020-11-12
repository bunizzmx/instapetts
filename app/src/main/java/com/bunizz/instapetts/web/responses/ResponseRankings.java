package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.EventBean;
import com.bunizz.instapetts.beans.RankingBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseRankings {

    @SerializedName("list_rankings")
    @Expose
    ArrayList<RankingBean> list_rankings ;

    @SerializedName("code_response")
    @Expose
    int code_response;

    public ResponseRankings() {
    }

    public ResponseRankings(ArrayList<RankingBean> list_rankings, int code_response) {
        this.list_rankings = list_rankings;
        this.code_response = code_response;
    }

    public ArrayList<RankingBean> getList_rankings() {
        return list_rankings;
    }

    public void setList_rankings(ArrayList<RankingBean> list_rankings) {
        this.list_rankings = list_rankings;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }
}

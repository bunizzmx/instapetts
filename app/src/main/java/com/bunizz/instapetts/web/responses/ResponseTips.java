package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.TipsBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseTips {

    @SerializedName("list_tips")
    @Expose
    ArrayList<TipsBean> list_tips;

    @SerializedName("helps")
    @Expose
    ArrayList<PostBean> helps;
    int code_response;

    public ResponseTips() {
    }

    public ResponseTips(ArrayList<TipsBean> list_tips, int code_response) {
        this.list_tips = list_tips;
        this.code_response = code_response;
    }

    public ArrayList<TipsBean> getList_tips() {
        return list_tips;
    }

    public void setList_tips(ArrayList<TipsBean> list_tips) {
        this.list_tips = list_tips;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }

    public ArrayList<PostBean> getHelps() {
        return helps;
    }

    public void setHelps(ArrayList<PostBean> helps) {
        this.helps = helps;
    }
}

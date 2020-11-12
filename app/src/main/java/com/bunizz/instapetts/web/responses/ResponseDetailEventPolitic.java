package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.RankingBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDetailEventPolitic {

    @SerializedName("politica")
    @Expose
    String politica ;

    @SerializedName("code_response")
    @Expose
    int code_response;

    public ResponseDetailEventPolitic() {
    }

    public ResponseDetailEventPolitic(String politica, int code_response) {
        this.politica = politica;
        this.code_response = code_response;
    }

    public String getPolitica() {
        return politica;
    }

    public void setPolitica(String politica) {
        this.politica = politica;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }
}

package com.bunizz.instapetts.web.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleResponse {

    @SerializedName("code_response")
    @Expose
    int code_response;

    @SerializedName("result_data_extra")
    @Expose
    String result_data_extra;

    public SimpleResponse() {}

    public SimpleResponse(int code_response) {
        this.code_response = code_response;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }

    public String getResult_data_extra() {
        return result_data_extra;
    }

    public void setResult_data_extra(String result_data_extra) {
        this.result_data_extra = result_data_extra;
    }
}

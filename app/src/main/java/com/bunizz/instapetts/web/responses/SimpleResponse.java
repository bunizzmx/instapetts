package com.bunizz.instapetts.web.responses;

public class SimpleResponse {
    int code_response;

    public SimpleResponse() {
    }

    public SimpleResponse(int code_response) {
        this.code_response = code_response;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }
}

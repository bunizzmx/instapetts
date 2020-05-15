package com.bunizz.instapetts.web.responses;

import java.util.ArrayList;

public class ResponseNamesAvailables {
    int available;
    int code_response;

    public ResponseNamesAvailables() {
    }

    public ResponseNamesAvailables(int available, int code_response) {
        this.available = available;
        this.code_response = code_response;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }
}

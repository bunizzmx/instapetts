package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.CodesCountryBean;

import java.util.ArrayList;

public class ResponseCodesCountries {
    ArrayList<CodesCountryBean> list_codes;
    int code_response;

    public ResponseCodesCountries() {
    }

    public ResponseCodesCountries(ArrayList<CodesCountryBean> list_codes, int code_response) {
        this.list_codes = list_codes;
        this.code_response = code_response;
    }

    public ArrayList<CodesCountryBean> getList_codes() {
        return list_codes;
    }

    public void setList_codes(ArrayList<CodesCountryBean> list_codes) {
        this.list_codes = list_codes;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }
}

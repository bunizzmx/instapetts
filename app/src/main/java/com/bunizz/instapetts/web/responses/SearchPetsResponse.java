package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.SearchPetBean;
import com.bunizz.instapetts.beans.SearchUserBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchPetsResponse {
    int code_response;

    @SerializedName("list_pets")
    @Expose
    ArrayList<SearchPetBean> list_pets;

    public SearchPetsResponse() {
    }

    public SearchPetsResponse(int code_response, ArrayList<SearchPetBean> list_pets) {
        this.code_response = code_response;
        this.list_pets = list_pets;
    }

    public ArrayList<SearchPetBean> getList_pets() {
        return list_pets;
    }

    public void setList_pets(ArrayList<SearchPetBean> list_pets) {
        this.list_pets = list_pets;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }

}

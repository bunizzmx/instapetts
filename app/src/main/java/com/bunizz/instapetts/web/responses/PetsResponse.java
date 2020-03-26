package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.PetBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PetsResponse {

    @SerializedName("code_response")
    @Expose
    int code_response;

    @SerializedName("list_pets")
    @Expose
    ArrayList<PetBean> list_pets = new ArrayList<>();

    public PetsResponse() {
    }

    public PetsResponse(int code_response, ArrayList<PetBean> list_pets) {
        this.code_response = code_response;
        this.list_pets = list_pets;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }

    public ArrayList<PetBean> getList_pets() {
        return list_pets;
    }

    public void setList_pets(ArrayList<PetBean> list_pets) {
        this.list_pets = list_pets;
    }
}

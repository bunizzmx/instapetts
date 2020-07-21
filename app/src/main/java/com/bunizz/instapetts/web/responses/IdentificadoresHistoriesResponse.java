package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class IdentificadoresHistoriesResponse {

    @SerializedName("identificadores")
    @Expose
    ArrayList<IdentificadoresHistoriesBean> identificadores;

    @SerializedName("ids_users")
    @Expose
    ArrayList<Integer> ids_users;

    @SerializedName("code_response")
    @Expose
    int code_response;

    public IdentificadoresHistoriesResponse() {
    }

    public IdentificadoresHistoriesResponse(ArrayList<IdentificadoresHistoriesBean> identificadores, int code_response) {
        this.identificadores = identificadores;
        this.code_response = code_response;
    }

    public ArrayList<IdentificadoresHistoriesBean> getIdentificadores() {
        return identificadores;
    }

    public void setIdentificadores(ArrayList<IdentificadoresHistoriesBean> identificadores) {
        this.identificadores = identificadores;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }

    public ArrayList<Integer> getIds_users() {
        return ids_users;
    }

    public void setIds_users(ArrayList<Integer> ids_users) {
        this.ids_users = ids_users;
    }
}

package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.RazaBean;

import java.util.ArrayList;

public class ResponseCatalogo {

    ArrayList<RazaBean> list_catalogo ;
    int code_response;

    public ResponseCatalogo() {
    }

    public ResponseCatalogo(ArrayList<RazaBean> list_catalogo, int code_response) {
        this.list_catalogo = list_catalogo;
        this.code_response = code_response;
    }

    public ArrayList<RazaBean> getList_catalogo() {
        return list_catalogo;
    }

    public void setList_catalogo(ArrayList<RazaBean> list_catalogo) {
        this.list_catalogo = list_catalogo;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }
}

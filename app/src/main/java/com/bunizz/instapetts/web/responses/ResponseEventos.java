package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.EventBean;
import com.bunizz.instapetts.beans.PlayVideos;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseEventos {

    @SerializedName("list_events")
    @Expose
    ArrayList<EventBean> list_events ;

    @SerializedName("code_response")
    @Expose
    int code_response;

    public ResponseEventos() {
    }

    public ResponseEventos(ArrayList<EventBean> list_events, int code_response) {
        this.list_events = list_events;
        this.code_response = code_response;
    }

    public ArrayList<EventBean> getList_Eventos() {
        return list_events;
    }

    public void setList_Events(ArrayList<EventBean> list_events) {
        this.list_events = list_events;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }
}

package com.bunizz.instapetts.managers;

import com.bunizz.instapetts.beans.PetBean;

import java.util.ArrayList;

public class FeedResponse {


    ArrayList<PetBean> data_response ;

    public ArrayList<PetBean> getData_response() {
        return data_response;
    }

    public void setData_response(ArrayList<PetBean> data_response) {
        this.data_response = data_response;
    }
}

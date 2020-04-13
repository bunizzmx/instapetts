package com.bunizz.instapetts.beans;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

@Parcel
public class HistoriesBean   implements Serializable {

    private static final long serialVersionUID = 8415742488977177498L;
    String name_user;
    int id_user;
    String url_photo_user;
    ArrayList<IndividualDataPetHistoryBean> histories;


    public HistoriesBean() {
    }

    public HistoriesBean(String name_user, int id_user, String url_photo_user, ArrayList<IndividualDataPetHistoryBean> histories) {
        this.name_user = name_user;
        this.id_user = id_user;
        this.url_photo_user = url_photo_user;
        this.histories = histories;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUrl_photo_user() {
        return url_photo_user;
    }

    public void setUrl_photo_user(String url_photo_user) {
        this.url_photo_user = url_photo_user;
    }

    public ArrayList<IndividualDataPetHistoryBean> getHistories() {
        return histories;
    }

    public void setHistories(ArrayList<IndividualDataPetHistoryBean> histories) {
        this.histories = histories;
    }
}

package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

@Parcel
public class HistoriesBean   implements Serializable {

    private static final long serialVersionUID = 8415742488977177498L;

    @SerializedName("name_user")
    @Expose
    String name_user;

    @SerializedName("id_user")
    @Expose
    int id_user;

    @SerializedName("photo_user")
    @Expose
    String photo_user;

    @SerializedName("historias")
    @Expose
    String historias;

    @SerializedName("ultima_fecha")
    @Expose
    String ultima_fecha;

    @SerializedName("target")
    @Expose
    String target;


    public HistoriesBean() {
    }

    public HistoriesBean(String name_user, int id_user, String photo_user, String historias, String ultima_fecha) {
        this.name_user = name_user;
        this.id_user = id_user;
        this.photo_user = photo_user;
        this.historias = historias;
        this.ultima_fecha = ultima_fecha;
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

    public String getPhoto_user() {
        return photo_user;
    }

    public void setPhoto_user(String photo_user) {
        this.photo_user = photo_user;
    }

    public String getUltima_fecha() {
        return ultima_fecha;
    }

    public void setUltima_fecha(String ultima_fecha) {
        this.ultima_fecha = ultima_fecha;
    }

    public String getHistorias() {
        return historias;
    }

    public void setHistorias(String historias) {
        this.historias = historias;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}

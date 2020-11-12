package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RankingBean {
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("name_user")
    @Expose
    String name_user;
    @SerializedName("id_user")
    @Expose
    int id_user;
    @SerializedName("name_pet")
    @Expose
    String name_pet;
    @SerializedName("id_pet")
    @Expose
    int id_pet;
    @SerializedName("url_photo_pet")
    @Expose
    String url_photo_pet;
    @SerializedName("likes")
    @Expose
    int likes;
    @SerializedName("name_raza")
    @Expose
    String name_raza;
    @SerializedName("type_pet")
    @Expose
    int type_pet;
    @SerializedName("date_updated")
    @Expose
    String date_updated;

    public RankingBean() {
    }

    public RankingBean(int id, String name_user, int id_user, String name_pet, int id_pet, String url_photo_pet, int likes, String name_raza, int type_pet, String date_updated) {
        this.id = id;
        this.name_user = name_user;
        this.id_user = id_user;
        this.name_pet = name_pet;
        this.id_pet = id_pet;
        this.url_photo_pet = url_photo_pet;
        this.likes = likes;
        this.name_raza = name_raza;
        this.type_pet = type_pet;
        this.date_updated = date_updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName_pet() {
        return name_pet;
    }

    public void setName_pet(String name_pet) {
        this.name_pet = name_pet;
    }

    public int getId_pet() {
        return id_pet;
    }

    public void setId_pet(int id_pet) {
        this.id_pet = id_pet;
    }

    public String getUrl_photo_pet() {
        return url_photo_pet;
    }

    public void setUrl_photo_pet(String url_photo_pet) {
        this.url_photo_pet = url_photo_pet;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getName_raza() {
        return name_raza;
    }

    public void setName_raza(String name_raza) {
        this.name_raza = name_raza;
    }

    public int getType_pet() {
        return type_pet;
    }

    public void setType_pet(int type_pet) {
        this.type_pet = type_pet;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }
}

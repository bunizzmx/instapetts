package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchPetBean {

    @SerializedName("id_pet")
    @Expose
    int id_pet;

    @SerializedName("id_user")
    @Expose
    int id_user;

    @SerializedName("uuid")
    @Expose
    String uuid;

    @SerializedName("url_photo")
    @Expose
    String url_photo;

    @SerializedName("name_user")
    @Expose
    String name_user;

    @SerializedName("name_pet")
    @Expose
    String name_pet;

    @SerializedName("name_raza")
    @Expose
    String name_raza;

    @SerializedName("type_raza")
    @Expose
    int type_raza;

    public SearchPetBean() {
    }

    public SearchPetBean(int id_pet, int id_user, String uuid, String url_photo, String name_user, String name_pet, String name_raza, int type_raza) {
        this.id_pet = id_pet;
        this.id_user = id_user;
        this.uuid = uuid;
        this.url_photo = url_photo;
        this.name_user = name_user;
        this.name_pet = name_pet;
        this.name_raza = name_raza;
        this.type_raza = type_raza;
    }

    public int getId_pet() {
        return id_pet;
    }

    public void setId_pet(int id_pet) {
        this.id_pet = id_pet;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUrl_photo() {
        return url_photo;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getName_pet() {
        return name_pet;
    }

    public void setName_pet(String name_pet) {
        this.name_pet = name_pet;
    }

    public String getName_raza() {
        return name_raza;
    }

    public void setName_raza(String name_raza) {
        this.name_raza = name_raza;
    }

    public int getType_raza() {
        return type_raza;
    }

    public void setType_raza(int type_raza) {
        this.type_raza = type_raza;
    }
}

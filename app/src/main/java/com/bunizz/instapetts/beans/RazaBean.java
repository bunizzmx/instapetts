package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RazaBean {

    @SerializedName("name_raza_esp")
    @Expose
    String name_raza_esp;

    @SerializedName("name_raza_eng")
    @Expose
    String name_raza_eng;

    @SerializedName("id_type_pet")
    @Expose
    int id_type_pet;

    @SerializedName("url_photo")
    @Expose
    String url_photo;

    public RazaBean() {
    }

    public RazaBean(String name_raza_esp, String name_raza_eng, int id_type_pet) {
        this.name_raza_esp = name_raza_esp;
        this.name_raza_eng = name_raza_eng;
        this.id_type_pet = id_type_pet;
    }

    public String getName_raza_esp() {
        return name_raza_esp;
    }

    public void setName_raza_esp(String name_raza_esp) {
        this.name_raza_esp = name_raza_esp;
    }

    public String getName_raza_eng() {
        return name_raza_eng;
    }

    public void setName_raza_eng(String name_raza_eng) {
        this.name_raza_eng = name_raza_eng;
    }

    public int getId_type_pet() {
        return id_type_pet;
    }

    public void setId_type_pet(int id_type_pet) {
        this.id_type_pet = id_type_pet;
    }

    public String getUrl_photo() {
        return url_photo;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }
}

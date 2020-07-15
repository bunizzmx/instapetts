package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class IndividualDataPetHistoryBean {

    @SerializedName("tumbh_video")
    @Expose
    String tumbh_video;

    @SerializedName("url_photo")
    @Expose
    String url_photo;

    @SerializedName("name_pet")
    @Expose
    String name_pet;

    @SerializedName("photo_pet")
    @Expose
    String photo_pet;

    @SerializedName("id_pet")
    @Expose
    int id_pet;

    @SerializedName("date_story")
    @Expose
    String date_story;

    @SerializedName("identificador")
    @Expose
    String identificador;

    int views;
    int likes;

    public IndividualDataPetHistoryBean() {
    }


    public IndividualDataPetHistoryBean(String name_pet,String url_photo ,String photo_pet, int id_pet,int views,String date_story) {
        this.url_photo = url_photo;
        this.name_pet = name_pet;
        this.photo_pet = photo_pet;
        this.id_pet = id_pet;
        this.date_story = date_story;
        this.views = views;
    }

    public IndividualDataPetHistoryBean(String name_pet,String photo_pet ,int id_pet,String url_photo,String identificador,String date_story) {
        this.url_photo = url_photo;
        this.name_pet = name_pet;
        this.photo_pet = photo_pet;
        this.id_pet = id_pet;
        this.date_story = date_story;
        this.identificador = identificador;
    }


    public int getId_pet() {
        return id_pet;
    }

    public void setId_pet(int id_pet) {
        this.id_pet = id_pet;
    }

    public String getUrl_photo() {
        return url_photo;
    }

    public String getDate_story() {
        return date_story;
    }

    public void setDate_story(String date_story) {
        this.date_story = date_story;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }

    public String getName_pet() {
        return name_pet;
    }

    public void setName_pet(String name_pet) {
        this.name_pet = name_pet;
    }

    public String getPhoto_pet() {
        return photo_pet;
    }

    public void setPhoto_pet(String photo_pet) {
        this.photo_pet = photo_pet;
    }

    public String getTumbh_video() {
        return tumbh_video;
    }

    public void setTumbh_video(String tumbh_video) {
        this.tumbh_video = tumbh_video;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}

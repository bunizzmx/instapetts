package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class IndividualDataPetHistoryBean implements  Comparable<IndividualDataPetHistoryBean> {

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

    @SerializedName("id_user")
    @Expose
    int id_user;

    @SerializedName("date_story")
    @Expose
    String date_story;

    @SerializedName("photo_user")
    @Expose
    String photo_user;

    @SerializedName("name_user")
    @Expose
    String name_user;

    @SerializedName("views")
    @Expose
    int views;

    public IndividualDataPetHistoryBean() {
    }


    public IndividualDataPetHistoryBean(String url_photo, String name_pet, String photo_pet, int id_pet, int id_user, String date_story, String photo_user, String name_user, int views) {
        this.url_photo = url_photo;
        this.name_pet = name_pet;
        this.photo_pet = photo_pet;
        this.id_pet = id_pet;
        this.id_user = id_user;
        this.date_story = date_story;
        this.photo_user = photo_user;
        this.name_user = name_user;
        this.views = views;
    }

    public String getPhoto_user() {
        return photo_user;
    }

    public void setPhoto_user(String photo_user) {
        this.photo_user = photo_user;
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


    @Override
    public int compareTo(IndividualDataPetHistoryBean individualDataPetHistoryBean) {
        return (this.getId_user() < individualDataPetHistoryBean.getId_user() ? -1 :
                (this.getId_user() == individualDataPetHistoryBean.getId_user() ? 0 : 1));
    }
}

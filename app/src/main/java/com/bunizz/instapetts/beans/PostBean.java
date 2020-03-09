package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostBean {
    @SerializedName("url_posts")
    @Expose
    String urls_posts;

    @SerializedName("name_pet")
    @Expose
    String name_pet;

    @SerializedName("name_user")
    @Expose
    String name_user;

    @SerializedName("url_photo_user")
    @Expose
    String url_photo_user;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("date_post")
    @Expose
    String date_post;


    public PostBean() {
    }

    public PostBean(String urls_posts, String name_pet, String name_user, String url_photo_user, String description) {
        this.urls_posts = urls_posts;
        this.name_pet = name_pet;
        this.name_user = name_user;
        this.url_photo_user = url_photo_user;
        this.description = description;
    }

    public String getDate_post() {
        return date_post;
    }

    public void setDate_post(String date_post) {
        this.date_post = date_post;
    }

    public String getUrl_photo_user() {
        return url_photo_user;
    }

    public void setUrl_photo_user(String url_photo_user) {
        this.url_photo_user = url_photo_user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrls_posts() {
        return urls_posts;
    }

    public void setUrls_posts(String urls_posts) {
        this.urls_posts = urls_posts;
    }

    public String getName_pet() {
        return name_pet;
    }

    public void setName_pet(String name_pet) {
        this.name_pet = name_pet;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }
}


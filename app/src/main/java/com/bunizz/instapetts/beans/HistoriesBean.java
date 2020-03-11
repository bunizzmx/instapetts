package com.bunizz.instapetts.beans;

public class HistoriesBean {
    String name_pet;
    String name_user;
    int id_pet;
    int id_user;
    String url_photo_pet;
    String url_photo_user;
    String uris_stories;
    int views;
    String date_story;

    public HistoriesBean() {
    }

    public HistoriesBean(String name_pet, String name_user, int id_pet, int id_user, String url_photo_pet, String url_photo_user, String uris_stories, int views, String date_story) {
        this.name_pet = name_pet;
        this.name_user = name_user;
        this.id_pet = id_pet;
        this.id_user = id_user;
        this.url_photo_pet = url_photo_pet;
        this.url_photo_user = url_photo_user;
        this.uris_stories = uris_stories;
        this.views = views;
        this.date_story = date_story;
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

    public String getUrl_photo_pet() {
        return url_photo_pet;
    }

    public void setUrl_photo_pet(String url_photo_pet) {
        this.url_photo_pet = url_photo_pet;
    }

    public String getUrl_photo_user() {
        return url_photo_user;
    }

    public void setUrl_photo_user(String url_photo_user) {
        this.url_photo_user = url_photo_user;
    }

    public String getUris_stories() {
        return uris_stories;
    }

    public void setUris_stories(String uris_stories) {
        this.uris_stories = uris_stories;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getDate_story() {
        return date_story;
    }

    public void setDate_story(String date_story) {
        this.date_story = date_story;
    }
}

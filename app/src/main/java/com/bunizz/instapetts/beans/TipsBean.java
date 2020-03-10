package com.bunizz.instapetts.beans;

public class TipsBean {
    int id;
    String body_tip;
    String title_tip;
    int type_pet;
    int raza_pet;
    String photo_tumbh_tip;
    String photo_tip;
    int views_tip;

    public TipsBean(int id, String body_tip, String title_tip, int type_pet, int raza_pet, String photo_tumbh_tip, String photo_tip, int views_tip) {
        this.id = id;
        this.body_tip = body_tip;
        this.title_tip = title_tip;
        this.type_pet = type_pet;
        this.raza_pet = raza_pet;
        this.photo_tumbh_tip = photo_tumbh_tip;
        this.photo_tip = photo_tip;
        this.views_tip = views_tip;
    }

    public TipsBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody_tip() {
        return body_tip;
    }

    public void setBody_tip(String body_tip) {
        this.body_tip = body_tip;
    }

    public String getTitle_tip() {
        return title_tip;
    }

    public void setTitle_tip(String title_tip) {
        this.title_tip = title_tip;
    }

    public int getType_pet() {
        return type_pet;
    }

    public void setType_pet(int type_pet) {
        this.type_pet = type_pet;
    }

    public int getRaza_pet() {
        return raza_pet;
    }

    public void setRaza_pet(int raza_pet) {
        this.raza_pet = raza_pet;
    }

    public String getPhoto_tumbh_tip() {
        return photo_tumbh_tip;
    }

    public void setPhoto_tumbh_tip(String photo_tumbh_tip) {
        this.photo_tumbh_tip = photo_tumbh_tip;
    }

    public String getPhoto_tip() {
        return photo_tip;
    }

    public void setPhoto_tip(String photo_tip) {
        this.photo_tip = photo_tip;
    }

    public int getViews_tip() {
        return views_tip;
    }

    public void setViews_tip(int views_tip) {
        this.views_tip = views_tip;
    }
}

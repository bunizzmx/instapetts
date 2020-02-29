package com.bunizz.instapetts.beans;

import android.content.ContentValues;

import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.db.helpers.PropietaryHelper;

public class PropietaryBean {
    String name;
    int posts;
    int followers;
    float rating;
    String descripcion;
    int numbers_pets;
    String url_photo;
    String id_propietary;

    public PropietaryBean(String name, int posts, int followers, float rating, String descripcion, int numbers_pets, String url_photo, String id_propietary) {
        this.name = name;
        this.posts = posts;
        this.followers = followers;
        this.rating = rating;
        this.descripcion = descripcion;
        this.numbers_pets = numbers_pets;
        this.url_photo = url_photo;
        this.id_propietary = id_propietary;
    }

    public String getId_propietary() {
        return id_propietary;
    }

    public void setId_propietary(String id_propietary) {
        this.id_propietary = id_propietary;
    }

    public String getUrl_photo() {
        return url_photo;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }

    public PropietaryBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNumbers_pets() {
        return numbers_pets;
    }

    public void setNumbers_pets(int numbers_pets) {
        this.numbers_pets = numbers_pets;
    }



    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PropietaryHelper.ID_PROPIETARY, getId_propietary());
        contentValues.put(PropietaryHelper.NAME_PROPIETARY, getName());
        contentValues.put(PropietaryHelper.RATING_PET, getRating());
        contentValues.put(PropietaryHelper.FOLOWERS, getFollowers());
        contentValues.put(PropietaryHelper.POST, getPosts());
        contentValues.put(PropietaryHelper.URL_PHOTO, getUrl_photo());
        contentValues.put(PropietaryHelper.DESCRIPCION, getDescripcion());
        return contentValues;
    }

}

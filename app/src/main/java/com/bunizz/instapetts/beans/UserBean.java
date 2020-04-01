package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserBean {

    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("name_user")
    @Expose
    String name_user;
    @SerializedName("ids_pets")
    @Expose
    String ids_pets;
    @SerializedName("descripcion")
    @Expose
    String descripcion;

    @SerializedName("num_pets")
    @Expose
    int num_pets;

    @SerializedName("photo_user")
    @Expose
    String photo_user;

    @SerializedName("photo_user_thumbh")
    @Expose
    String photo_user_thumbh;

    @SerializedName("phone_user")
    @Expose
    String phone_user;

    @SerializedName("lat")
    @Expose
    double lat;

    @SerializedName("lon")
    @Expose
    double lon;

    @SerializedName("token")
    @Expose
    String token;

    @SerializedName("uuid")
    @Expose
    String uuid;

    @SerializedName("posts")
    @Expose
    int posts;

    @SerializedName("rate_pets")
    @Expose
    double rate_pets;

    @SerializedName("followers")
    @Expose
    int folowers;

    @SerializedName("date_active")
    @Expose
    String date_active;

    @SerializedName("android_id")
    @Expose
    String android_id;

    String target;

    public UserBean() {
    }

    public UserBean(int id, String name_user, String ids_pets, String descripcion, int num_pets, String photo_user, String phone_user, double lat, double lon, String token, String uuid, int posts, double rate_pets, int folowers) {
        this.id = id;
        this.name_user = name_user;
        this.ids_pets = ids_pets;
        this.descripcion = descripcion;
        this.num_pets = num_pets;
        this.photo_user = photo_user;
        this.phone_user = phone_user;
        this.lat = lat;
        this.lon = lon;
        this.token = token;
        this.uuid = uuid;
        this.posts = posts;
        this.rate_pets = rate_pets;
        this.folowers = folowers;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public String getIds_pets() {
        return ids_pets;
    }

    public void setIds_pets(String ids_pets) {
        this.ids_pets = ids_pets;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNum_pets() {
        return num_pets;
    }

    public void setNum_pets(int num_pets) {
        this.num_pets = num_pets;
    }

    public String getPhoto_user() {
        return photo_user;
    }

    public void setPhoto_user(String photo_user) {
        this.photo_user = photo_user;
    }

    public String getPhone_user() {
        return phone_user;
    }

    public void setPhone_user(String phone_user) {
        this.phone_user = phone_user;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public double getRate_pets() {
        return rate_pets;
    }

    public void setRate_pets(double rate_pets) {
        this.rate_pets = rate_pets;
    }

    public int getFolowers() {
        return folowers;
    }

    public void setFolowers(int folowers) {
        this.folowers = folowers;
    }

    public String getDate_active() {
        return date_active;
    }

    public void setDate_active(String date_active) {
        this.date_active = date_active;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }

    public String getPhoto_user_thumbh() {
        return photo_user_thumbh;
    }

    public void setPhoto_user_thumbh(String photo_user_thumbh) {
        this.photo_user_thumbh = photo_user_thumbh;
    }
}

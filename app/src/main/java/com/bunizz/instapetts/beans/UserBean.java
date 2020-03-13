package com.bunizz.instapetts.beans;

public class UserBean {
    int id;
    String name_user;
    String ids_pets;
    String descripcion;
    int num_pets;
    String photo_user;
    String phone_user;
    double lat;
    double lon;
    String token;
    String uuid;
    String target;

    public UserBean() {
    }

    public UserBean(int id, String name_user, String ids_pets, String descripcion, int num_pets, String photo_user, String phone_user, double lat, double lon, String token, String uuid) {
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
}

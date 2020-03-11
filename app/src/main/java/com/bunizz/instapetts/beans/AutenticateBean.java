package com.bunizz.instapetts.beans;

public class AutenticateBean {

    String name_user;
    String token;

    public AutenticateBean() {
    }

    public AutenticateBean(String name_user, String token) {
        this.name_user = name_user;
        this.token = token;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

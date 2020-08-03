package com.bunizz.instapetts.beans;

import org.parceler.Parcel;

@Parcel
public class AutenticateBean {

    String name_user;
    String token;
    String target;
    int paginador;

    public AutenticateBean() {
    }

    public AutenticateBean(String name_user, String token) {
        this.name_user = name_user;
        this.token = token;
    }

    public AutenticateBean(String name_user, String token, String target) {
        this.name_user = name_user;
        this.token = token;
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public int getPaginador() {
        return paginador;
    }

    public void setPaginador(int paginador) {
        this.paginador = paginador;
    }
}

package com.bunizz.instapetts.beans;

import org.parceler.Parcel;

@Parcel
public class AutenticateBean {

    String name_user;
    String token;
    String target;
    int paginador;
    String idioma;
    int id_usuario;

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

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setPaginador(int paginador) {
        this.paginador = paginador;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
}

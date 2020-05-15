package com.bunizz.instapetts.beans;

public class IdentificadoresHistoriesBean {
    String identificador;
    int num_views;
    int num_likes;

    public IdentificadoresHistoriesBean() {
    }

    public IdentificadoresHistoriesBean(String identificador, int num_views, int num_likes) {
        this.identificador = identificador;
        this.num_views = num_views;
        this.num_likes = num_likes;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public int getNum_views() {
        return num_views;
    }

    public void setNum_views(int num_views) {
        this.num_views = num_views;
    }

    public int getNum_likes() {
        return num_likes;
    }

    public void setNum_likes(int num_likes) {
        this.num_likes = num_likes;
    }
}

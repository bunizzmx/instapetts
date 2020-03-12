package com.bunizz.instapetts.web.parameters;

public class PostActions {

    int id_post;
    String acccion;
    String valor;
    String id_usuario;

    public PostActions() {
    }

    public PostActions(int id_post, String acccion, String valor, String id_usuario) {
        this.id_post = id_post;
        this.acccion = acccion;
        this.valor = valor;
        this.id_usuario = id_usuario;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public String getAcccion() {
        return acccion;
    }

    public void setAcccion(String acccion) {
        this.acccion = acccion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
}

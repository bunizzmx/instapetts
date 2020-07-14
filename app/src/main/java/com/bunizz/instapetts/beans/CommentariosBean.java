package com.bunizz.instapetts.beans;

import org.parceler.Parcel;

@Parcel
public class CommentariosBean {

    String commentario;
    String fecha_comentario;
    String name_user;
    String foto_user;
    int id_user;
    int id_post;
    boolean is_liked;
    int likes;
    String id_document;
    int helps_post;

    public CommentariosBean() {
    }

    public CommentariosBean(String commentario, String fecha_comentario, String name_user, String foto_user, int id_user) {
        this.commentario = commentario;
        this.fecha_comentario = fecha_comentario;
        this.name_user = name_user;
        this.foto_user = foto_user;
        this.id_user = id_user;
    }

    public String getCommentario() {
        return commentario;
    }

    public void setCommentario(String commentario) {
        this.commentario = commentario;
    }

    public String getFecha_comentario() {
        return fecha_comentario;
    }

    public void setFecha_comentario(String fecha_comentario) {
        this.fecha_comentario = fecha_comentario;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getFoto_user() {
        return foto_user;
    }

    public void setFoto_user(String foto_user) {
        this.foto_user = foto_user;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public boolean isIs_liked() {
        return is_liked;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getId_document() {
        return id_document;
    }

    public void setId_document(String id_document) {
        this.id_document = id_document;
    }

    public int getHelps_post() {
        return helps_post;
    }

    public void setHelps_post(int helps_post) {
        this.helps_post = helps_post;
    }
}

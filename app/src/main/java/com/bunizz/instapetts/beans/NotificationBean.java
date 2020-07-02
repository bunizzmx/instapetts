package com.bunizz.instapetts.beans;


import org.parceler.Parcel;

@Parcel
public class NotificationBean {
String title;
String body;
String url_resource;
String url_image_extra;
String fecha;
String id_document_notification;
int type_notification;
int id_usuario;
int id_recurso;
boolean is_open;
int id_database;

    public NotificationBean() {
    }

    public NotificationBean(String title, String body, String url_resource, int type_notification, int id_usuario,String url_image_extra,String fecha) {
        this.title = title;
        this.body = body;
        this.url_resource = url_resource;
        this.type_notification = type_notification;
        this.id_usuario = id_usuario;
        this.fecha = fecha;
        this.url_image_extra =url_image_extra;
    }

    public boolean isIs_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl_resource() {
        return url_resource;
    }

    public void setUrl_resource(String url_resource) {
        this.url_resource = url_resource;
    }

    public int getType_notification() {
        return type_notification;
    }

    public void setType_notification(int type_notification) {
        this.type_notification = type_notification;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_database() {
        return id_database;
    }

    public void setId_database(int id_database) {
        this.id_database = id_database;
    }

    public String getUrl_image_extra() {
        return url_image_extra;
    }

    public void setUrl_image_extra(String url_image_extra) {
        this.url_image_extra = url_image_extra;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getId_document_notification() {
        return id_document_notification;
    }

    public void setId_document_notification(String id_document_notification) {
        this.id_document_notification = id_document_notification;
    }

    public int getId_recurso() {
        return id_recurso;
    }

    public void setId_recurso(int id_recurso) {
        this.id_recurso = id_recurso;
    }
}

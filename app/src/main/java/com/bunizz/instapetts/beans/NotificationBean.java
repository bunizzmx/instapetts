package com.bunizz.instapetts.beans;

public class NotificationBean {
String title;
String body;
String url_resource;
String url_image_extra;
int type_notification;
int id_usuario;
boolean is_open;
int id_database;

    public NotificationBean() {
    }

    public NotificationBean(String title, String body, String url_resource, int type_notification, int id_usuario) {
        this.title = title;
        this.body = body;
        this.url_resource = url_resource;
        this.type_notification = type_notification;
        this.id_usuario = id_usuario;
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
}

package com.bunizz.instapetts.beans;

public class DataEmptyBean {
    String title;
    String body;

    public DataEmptyBean() {
    }

    public DataEmptyBean(String title, String body) {
        this.title = title;
        this.body = body;
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
}

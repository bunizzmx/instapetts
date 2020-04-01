package com.bunizz.instapetts.web.parameters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostLikeBean {
    @SerializedName("id_post")
    @Expose
    int id_post;

    @SerializedName("type_event")
    @Expose
    int type_event;

    @SerializedName("target")
    @Expose
    String target;

    public PostLikeBean() {
    }

    public PostLikeBean(int id_post, int type_event, String target) {
        this.id_post = id_post;
        this.type_event = type_event;
        this.target = target;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public int getType_event() {
        return type_event;
    }

    public void setType_event(int type_event) {
        this.type_event = type_event;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}

package com.bunizz.instapetts.web.parameters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowParameter {

    @SerializedName("id_user")
    @Expose
    int id_user;

    @SerializedName("target")
    @Expose
    String target;

    public FollowParameter() {
    }

    public FollowParameter(int id_user, String target) {
        this.id_user = id_user;
        this.target = target;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}

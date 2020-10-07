package com.bunizz.instapetts.web.parameters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayVideoParameters {

    @SerializedName("target")
    @Expose
    String target;

    @SerializedName("id_video")
    @Expose
    int id_video;

    public PlayVideoParameters() {
    }

    public PlayVideoParameters(String target, int id_video) {
        this.target = target;
        this.id_video = id_video;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getId_video() {
        return id_video;
    }

    public void setId_video(int id_video) {
        this.id_video = id_video;
    }
}

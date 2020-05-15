package com.bunizz.instapetts.web.parameters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParameterAvailableNames {
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("target")
    @Expose
    String target;

    public ParameterAvailableNames() {
    }

    public ParameterAvailableNames(String name, String target) {
        this.name = name;
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}

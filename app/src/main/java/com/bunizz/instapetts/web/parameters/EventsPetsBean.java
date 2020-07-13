package com.bunizz.instapetts.web.parameters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventsPetsBean {
    String target;
    int id_pet;
    int stars;

    @SerializedName("id_propietary")
    @Expose
    int id_propietary;

    public EventsPetsBean() {
    }

    public EventsPetsBean(String target, int id_pet) {
        this.target = target;
        this.id_pet = id_pet;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getId_pet() {
        return id_pet;
    }

    public void setId_pet(int id_pet) {
        this.id_pet = id_pet;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getId_propietary() {
        return id_propietary;
    }

    public void setId_propietary(int id_propietary) {
        this.id_propietary = id_propietary;
    }
}

package com.bunizz.instapetts.web.parameters;

public class EventsPetsBean {
    String target;
    int id_pet;
    int stars;

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
}

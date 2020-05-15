package com.bunizz.instapetts.web.parameters;

public class EventsTipsBean {

    String target;
    int id_tip;

    public EventsTipsBean() {
    }

    public EventsTipsBean(String target, int id_tip) {
        this.target = target;
        this.id_tip = id_tip;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getId_tip() {
        return id_tip;
    }

    public void setId_tip(int id_tip) {
        this.id_tip = id_tip;
    }
}

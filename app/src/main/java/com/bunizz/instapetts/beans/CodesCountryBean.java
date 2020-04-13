package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CodesCountryBean {
    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("name_ing")
    @Expose
    String name_ing;

    @SerializedName("num")
    @Expose
    String num;

    public CodesCountryBean() {
    }

    public CodesCountryBean(String name, String name_ing, String num) {
        this.name = name;
        this.name_ing = name_ing;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_ing() {
        return name_ing;
    }

    public void setName_ing(String name_ing) {
        this.name_ing = name_ing;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}

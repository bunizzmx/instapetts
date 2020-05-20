package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ReportListBean {

    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("name")
    @Expose
    String  name;

    @SerializedName("name_ing")
    @Expose
    String name_ing;

    public ReportListBean() {
    }

    public ReportListBean(int id, String name, String name_ing) {
        this.id = id;
        this.name = name;
        this.name_ing = name_ing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}

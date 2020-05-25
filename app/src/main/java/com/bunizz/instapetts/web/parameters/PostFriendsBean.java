package com.bunizz.instapetts.web.parameters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostFriendsBean {
    @SerializedName("uuid")
    @Expose
    ArrayList<String> uuids;

    @SerializedName("ids")
    @Expose
    ArrayList<Integer>ids;

    @SerializedName("ids_h")
    @Expose
    ArrayList<Integer>ids_h;

    @SerializedName("target")
    @Expose
    String target;

    @SerializedName("filter")
    @Expose
    int filter;

    @SerializedName("id_one")
    @Expose
    int id_one;

    @SerializedName("paginador")
    @Expose
    int paginador;

    public PostFriendsBean() {
    }

    public PostFriendsBean(ArrayList<String> uuids, ArrayList<Integer> ids, String target, int id_one) {
        this.uuids = uuids;
        this.ids = ids;
        this.target = target;
        this.id_one = id_one;
    }

    public ArrayList<String> getUuids() {
        return uuids;
    }

    public void setUuids(ArrayList<String> uuids) {
        this.uuids = uuids;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public ArrayList<Integer> getIds_h() {
        return ids_h;
    }

    public void setIds_h(ArrayList<Integer> ids_h) {
        this.ids_h = ids_h;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getId_one() {
        return id_one;
    }

    public void setId_one(int id_one) {
        this.id_one = id_one;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public int getPaginador() {
        return paginador;
    }

    public void setPaginador(int paginador) {
        this.paginador = paginador;
    }
}

package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowsBean {

    @SerializedName("id_user")
    @Expose
    int id_user;

    @SerializedName("uuid_user")
    @Expose
    String uuid_user;

    @SerializedName("url_photo_user")
    @Expose
    String url_photo_user;

    @SerializedName("name_user")
    @Expose
    String name_user;

    @SerializedName("name_nip_user")
    @Expose
    String name_nip_user;

    public FollowsBean() {
    }

    public FollowsBean(int id_user, String uuid_user, String url_photo_user, String name_user, String name_nip_user) {
        this.id_user = id_user;
        this.uuid_user = uuid_user;
        this.url_photo_user = url_photo_user;
        this.name_user = name_user;
        this.name_nip_user = name_nip_user;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUuid_user() {
        return uuid_user;
    }

    public void setUuid_user(String uuid_user) {
        this.uuid_user = uuid_user;
    }

    public String getUrl_photo_user() {
        return url_photo_user;
    }

    public void setUrl_photo_user(String url_photo_user) {
        this.url_photo_user = url_photo_user;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getName_nip_user() {
        return name_nip_user;
    }

    public void setName_nip_user(String name_nip_user) {
        this.name_nip_user = name_nip_user;
    }
}

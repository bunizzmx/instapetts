package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchUserBean {

    @SerializedName("id_user")
    @Expose
    String id_user;

    @SerializedName("uudi")
    @Expose
    String uudi;

    @SerializedName("url_photo")
    @Expose
    String url_photo;

    @SerializedName("name_user")
    @Expose
    String name_user;

    @SerializedName("name_tag")
    @Expose
    String name_tag;

    public SearchUserBean() {
    }

    public SearchUserBean(String id_user, String uudi, String url_photo, String name_user, String name_tag) {
        this.id_user = id_user;
        this.uudi = uudi;
        this.url_photo = url_photo;
        this.name_user = name_user;
        this.name_tag = name_tag;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getUudi() {
        return uudi;
    }

    public void setUudi(String uudi) {
        this.uudi = uudi;
    }

    public String getUrl_photo() {
        return url_photo;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getName_tag() {
        return name_tag;
    }

    public void setName_tag(String name_tag) {
        this.name_tag = name_tag;
    }


}

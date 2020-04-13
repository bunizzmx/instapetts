package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class PostBean {
    @SerializedName("url_posts")
    @Expose
    String urls_posts;

    @SerializedName("thumb_video")
    @Expose
    String thumb_video;

    @SerializedName("name_pet")
    @Expose
    String name_pet;

    @SerializedName("name_user")
    @Expose
    String name_user;

    @SerializedName("url_photo_user")
    @Expose
    String url_photo_user;

    @SerializedName("url_photo_pet")
    @Expose
    String url_photo_pet;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("date_post")
    @Expose
    String date_post;

    @SerializedName("uuid")
    @Expose
    String uuid;

    @SerializedName("uuidbucket")
    @Expose
    String uuidbucket;

    @SerializedName("id_usuario")
    @Expose
    int id_usuario;

    @SerializedName("id_pet")
    @Expose
    int id_pet;

    @SerializedName("likes")
    @Expose
    int likes;

    @SerializedName("type_post")
    @Expose
    int type_post;

    @SerializedName("type_pet")
    @Expose
    int type_pet;

    @SerializedName("id_post")
    @Expose
    int id_post_from_web;

    @SerializedName("address")
    @Expose
    String address;

    boolean saved;
    boolean liked;

    String target;

    public PostBean() {
    }

    public PostBean(String urls_posts, String name_pet, String name_user, String url_photo_user, String description) {
        this.urls_posts = urls_posts;
        this.name_pet = name_pet;
        this.name_user = name_user;
        this.url_photo_user = url_photo_user;
        this.description = description;
    }

    public PostBean(String urls_posts, String name_pet, String name_user, String url_photo_user, String url_photo_pet, String description, String date_post, String uuid, String uuidbucket, int id_usuario, int id_pet, int likes, int id_post_from_web, String address) {
        this.urls_posts = urls_posts;
        this.name_pet = name_pet;
        this.name_user = name_user;
        this.url_photo_user = url_photo_user;
        this.url_photo_pet = url_photo_pet;
        this.description = description;
        this.date_post = date_post;
        this.uuid = uuid;
        this.uuidbucket = uuidbucket;
        this.id_usuario = id_usuario;
        this.id_pet = id_pet;
        this.likes = likes;
        this.id_post_from_web = id_post_from_web;
        this.address = address;
    }

    public int getId_post_from_web() {
        return id_post_from_web;
    }

    public void setId_post_from_web(int id_post_from_web) {
        this.id_post_from_web = id_post_from_web;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getDate_post() {
        return date_post;
    }

    public void setDate_post(String date_post) {
        this.date_post = date_post;
    }

    public String getUrl_photo_user() {
        return url_photo_user;
    }

    public void setUrl_photo_user(String url_photo_user) {
        this.url_photo_user = url_photo_user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrls_posts() {
        return urls_posts;
    }

    public void setUrls_posts(String urls_posts) {
        this.urls_posts = urls_posts;
    }

    public String getName_pet() {
        return name_pet;
    }

    public void setName_pet(String name_pet) {
        this.name_pet = name_pet;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUrl_photo_pet() {
        return url_photo_pet;
    }

    public void setUrl_photo_pet(String url_photo_pet) {
        this.url_photo_pet = url_photo_pet;
    }

    public int getId_pet() {
        return id_pet;
    }

    public void setId_pet(int id_pet) {
        this.id_pet = id_pet;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getUuidbucket() {
        return uuidbucket;
    }

    public void setUuidbucket(String uuidbucket) {
        this.uuidbucket = uuidbucket;
    }

    public int getType_post() {
        return type_post;
    }

    public void setType_post(int type_post) {
        this.type_post = type_post;
    }

    public int getType_pet() {
        return type_pet;
    }

    public void setType_pet(int type_pet) {
        this.type_pet = type_pet;
    }

    public String getThumb_video() {
        return thumb_video;
    }

    public void setThumb_video(String thumb_video) {
        this.thumb_video = thumb_video;
    }
}


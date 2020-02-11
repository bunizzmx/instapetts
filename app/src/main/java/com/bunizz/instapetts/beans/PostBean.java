package com.bunizz.instapetts.beans;

import java.util.ArrayList;

public class PostBean {
    ArrayList<String> urls_pet;

    public PostBean() {
    }

    public PostBean(ArrayList<String> urls_pet) {
        this.urls_pet = urls_pet;
    }

    public ArrayList<String> getUrls_pet() {
        return urls_pet;
    }

    public void setUrls_pet(ArrayList<String> urls_pet) {
        this.urls_pet = urls_pet;
    }
}

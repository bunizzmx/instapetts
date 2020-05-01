package com.bunizz.instapetts.beans;

public class SelectedsImagesBean {

    String path;
    boolean is_selected;

    public SelectedsImagesBean(String path, boolean is_selected) {
        this.path = path;
        this.is_selected = is_selected;
    }

    public SelectedsImagesBean() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }
}

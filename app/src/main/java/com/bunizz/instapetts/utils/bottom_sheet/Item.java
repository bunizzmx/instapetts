package com.bunizz.instapetts.utils.bottom_sheet;


import androidx.annotation.DrawableRes;

public class Item {

    private int mDrawableRes;

    private String mTitle;
    int type_menu;

    public Item(@DrawableRes int drawable, String title, int type_menu) {
        mDrawableRes = drawable;
        mTitle = title;
        this.type_menu = type_menu;
    }

    public int getDrawableResource() {
        return mDrawableRes;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getmDrawableRes() {
        return mDrawableRes;
    }

    public void setmDrawableRes(int mDrawableRes) {
        this.mDrawableRes = mDrawableRes;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getType_menu() {
        return type_menu;
    }

    public void setType_menu(int type_menu) {
        this.type_menu = type_menu;
    }
}

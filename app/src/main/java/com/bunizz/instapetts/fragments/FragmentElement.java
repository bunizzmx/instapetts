package com.bunizz.instapetts.fragments;


import androidx.fragment.app.Fragment;

/**
 * Created by jduranpc on 11/16/16.
 */

public class FragmentElement<T extends Fragment> {
    //region Variables
    //region Statics
    public static final int INSTANCE_MAIN_LOGIN = 1;
    public static final int INSTANCE_SIGIN = 2;
    public static final int INSTANCE_LOGIN = 3;
    public static final int INSTANCE_FEED = 4;
    public static final int INSTANCE_PROFILE_PET= 5;
    public static final int INSTANCE_TYPE_PET = 6;
    public static final int INSTANCE_TYPE_SEARCH_RAZA = 7;
    public static final int INSTANCE_DATA_PET= 8;
    public static final int INSTANCE_TIPS= 9;
    public static final int INSTANCE_SEARCH= 10;
    public static final int INSTANCE_NOTIFICATIONS= 11;
    public static final int INSTANCE_TIP_DETAIL= 12;
    public static final int INSTANCE_SHARE= 13;
    public static final int INSTANCE_PICKER_IMAGES= 14;
    public static final int INSTANCE_HISTORY_CAMERA= 15;
    public static final int INSTANCE_HISTORY_FOTO_PICKED= 16;
    public static final int INSTANCE_CONFIGURE_DATA_USER= 17;

    public static final int INSTANCE_DETECT_QR= 18;
    public static final int INSTANCE_VIEW_MY_QR= 19;
    public static final int INSTANCE_PREVIEW_PROFILE= 20;
    public static final int INSTANCE_EDIT_PROFILE_USER= 21;

    public static final int INSTANCE_PICKER_VIDEOS= 22;
    public static final int INSTANCE_PICKER_CAMERA= 23;

    public static final int INSTANCE_CROP_VIDEO= 24;
    public static final int INSTANCE_CROP_IMAGE= 25;
    public static final int INSTANCE_FINAL_CONFIG_PET= 26;
    public static final int INSTANCE_NEW_USER_CONFIG= 27;

    public static final int INSTANCE_GET_POSTS_PUBLICS= 28;
    public static final int INSTANCE_GET_POSTS_PUBLICS_ADVANCED= 29;


    public static final int INSTANCE_WEB_TERMS = 30;
    public static final int INSTANCE_ADMINISTRATE_ACCOUNT = 31;
    public static final int INSTANCE_ADMINISTRATE_PHONE = 32;
    public static final int INSTANCE_ADMINISTRATE_EMAIL= 33;
    public static final int INSTANCE_ADMINISTRATE_PASSWORD = 34;
    public static final int INSTANCE_COONFIG_PUSH = 35;
    public static final int INSTANCE_FOLLOWS_USER = 36;
    public static final int INSTANCE_COUNTRY_CODES= 37;
    public static final int INSTANCE_COMENTARIOS =38;

    public static final int INSTANCE_REPORTS_LIST =39;
    public static final int INSTANCE_FINAL_REPORT =40;

    public static final int INSTANCE_SIDE_MENU = 41;

    public static final int INSTANCE_PLAY_VIDEOS = 42;
    public static final int INSTANCE_EVENTOS = 43;
    //endregion
    private String mTitle;
    private T fragment;
    private int instanceType;
    private boolean map;
    //endregion

    //region Constructor
    public FragmentElement(String title, T fragment, int instanceType) {
        this.mTitle = title;
        this.fragment = fragment;
        this.instanceType = instanceType;
    }

    public FragmentElement(String title, T fragment, int instanceType, boolean map) {
        this.mTitle = title;
        this.fragment = fragment;
        this.instanceType = instanceType;
        this.map = map;
    }
    //endregion

    //region Getters
    public String getTitle() {
        return mTitle;
    }

    public boolean isMap() {
        return map;
    }

    public void setMap(boolean map) {
        this.map = map;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public int getInstanceType() {
        return instanceType;
    }

    //endregion
}

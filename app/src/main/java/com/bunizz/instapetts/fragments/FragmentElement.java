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

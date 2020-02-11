package com.bunizz.instapetts.utils.tabs;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public abstract class SlidingFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final String EMPTY_TOOLBAR_TITLE = "";

    public SlidingFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Drawable getPageDrawable(int position) {
        return null;
    }

    public String getToolbarTitle(int position) {
        return EMPTY_TOOLBAR_TITLE;
    }
}

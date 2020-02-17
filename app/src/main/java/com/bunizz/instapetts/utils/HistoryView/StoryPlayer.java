package com.bunizz.instapetts.utils.HistoryView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.story.FragmentStoriView;
import com.bunizz.instapetts.listeners.story_finished_listener;
import com.bunizz.instapetts.utils.ViewPagerHistory.DepthPageTransformer;
import com.bunizz.instapetts.utils.tabs.SlidingFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoryPlayer extends AppCompatActivity implements story_finished_listener {
    public static final String STORY_IMAGE_KEY = "storyImages";
    @BindView(R.id.view_pager_stories)
    ViewPager view_pager_stories;
    private TabAdapter adapter;
    int CURRENT_ITEM =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_activity);
        ButterKnife.bind(this);
        changeStatusBarColor(R.color.black);
        adapter = new TabAdapter(getSupportFragmentManager(), this);
        view_pager_stories.setAdapter(adapter);
        view_pager_stories.setOffscreenPageLimit(0);
        view_pager_stories.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                 CURRENT_ITEM = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        view_pager_stories.setPageTransformer(true, new DepthPageTransformer());
    }


    @Override
    protected void onPause() {
        super.onPause();
    }



    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), color));
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void on_finish() {
        view_pager_stories.setCurrentItem(CURRENT_ITEM + 1);
    }


    public class TabAdapter extends SlidingFragmentPagerAdapter {


        private int[] icons = {
                R.drawable.ic_menu,
                R.drawable.ic_favorito,
                R.drawable.ic_lista
        };
        private Context context;

        public TabAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("x", position + 1);
            Fragment fragment;
            fragment = new FragmentStoriView();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 10;
        }


        @Override
        public Drawable getPageDrawable(int position) {
            return context.getResources().getDrawable(icons[position]);
        }

    }
}

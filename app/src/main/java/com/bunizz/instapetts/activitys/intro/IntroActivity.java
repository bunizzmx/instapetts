package com.bunizz.instapetts.activitys.intro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bunizz.instapetts.App;
import com.bunizz.instapetts.MainActivity;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.intro.Fragment1;
import com.bunizz.instapetts.fragments.intro.Fragment2;
import com.bunizz.instapetts.fragments.intro.Fragment3;
import com.bunizz.instapetts.listeners.VisibleItem;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class IntroActivity extends AppCompatActivity implements VisibleItem {
    DotsIndicator dotsIndicator ;
    ViewPager viewPager ;
    PagerAdapter pagerAdapter;
    Fragment[] fragments_data = null;
    TextView back,skip;
    RelativeLayout indicators;
    FragmentManager fragmentManager;
    ProgressBar progress_intro;
    String data_fecha = App.formatDateGMT(new Date());
    int RETRY =0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.intro_activity);
            changeStatusBarColor(R.color.white);
            dotsIndicator = findViewById(R.id.dots_indicator);
            viewPager = findViewById(R.id.viewpager);
            skip = findViewById(R.id.skip);
            back = findViewById(R.id.back);
            indicators =findViewById(R.id.indicators);
            fragmentManager = getSupportFragmentManager();
            fragments_data = new Fragment[]{
                    fragmentManager.getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), Fragment1.class.getName()),
                    fragmentManager.getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), Fragment2.class.getName()),
                    fragmentManager.getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), Fragment3.class.getName())
            };

            progress_intro = findViewById(R.id.progress_intro);
            pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments_data);
            pagerAdapter.notifyDataSetChanged();
            viewPager.setAdapter(pagerAdapter);
            viewPager.setOnTouchListener((arg0, arg1) -> true);
            dotsIndicator.setViewPager(viewPager);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if(position==0){
                        back.setVisibility(View.GONE);
                    }else if(position ==2){
                            indicators.setVisibility(View.VISIBLE);
                            skip.setVisibility(View.VISIBLE);
                            back.setVisibility(View.VISIBLE);
                            dotsIndicator.setVisibility(View.VISIBLE);

                    }

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            back.setOnClickListener(view -> {
               finish();
            });


            skip.setOnClickListener(view -> {
                int position = viewPager.getCurrentItem();
                if(position<fragments_data.length-1) {
                    viewPager.setCurrentItem(position +1);
                    back.setText(getResources().getString(R.string.exit));
                    skip.setText(getResources().getString(R.string.acept_next));
                }else{
                    progress_intro.setVisibility(View.VISIBLE);
                    skip.setVisibility(View.GONE);
                }

            });



    }

    @Override
    public void onSUccess(boolean success) {
        skip.setVisibility(View.VISIBLE);
    }


    public class PagerAdapter extends FragmentPagerAdapter {

        private final Fragment[] fragments;

        public PagerAdapter(FragmentManager fragmentManager, Fragment[] fragments) {
            super(fragmentManager);
            this.fragments = fragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position].getClass().getSimpleName();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }


    @Override
    public void onBackPressed() {
        int position = viewPager.getCurrentItem();
        if(position > 0 ){
            viewPager.setCurrentItem(position-1);
        }else{
            super.onBackPressed();
        }
    }





    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), color));
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }
    }


}

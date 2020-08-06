package com.bunizz.instapetts.activitys.intro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.db.Utilities;
import com.bunizz.instapetts.fragments.intro.Fragment1;
import com.bunizz.instapetts.fragments.intro.Fragment2;
import com.bunizz.instapetts.fragments.intro.Fragment3;
import com.bunizz.instapetts.listeners.VisibleItem;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bunizz.instapetts.constantes.PREFERENCES.IS_INTRO_COMPLETED;

public class IntroActivity extends AppCompatActivity implements VisibleItem {
    DotsIndicator dotsIndicator ;
    ViewPager viewPager ;
    PagerAdapter pagerAdapter;
    Fragment[] fragments_data = null;
    Button skip,back,omitter;
    FragmentManager fragmentManager;
    ProgressBar progress_intro;


    @BindView(R.id.indicators)
    RelativeLayout indicators;


    String data_fecha = App.formatDateGMT(new Date());
    int RETRY =0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.intro_activity);
            ButterKnife.bind(this);
            View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener
                (visibility -> {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        move_pager_up();
                       Log.e("BARRA_ESTATUS","barra visible");
                    } else {
                        move_pager_down();
                        Log.e("BARRA_ESTATUS","barra oculta");
                    }
                });

            //changeStatusBarColor(R.color.white);
            dotsIndicator = findViewById(R.id.dots_indicator);
            viewPager = findViewById(R.id.viewpager);
            skip = findViewById(R.id.skip);
            back = findViewById(R.id.back);
            omitter = findViewById(R.id.omitter);
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
                    }else if(position ==1){
                        back.setVisibility(View.VISIBLE);
                    }else{
                        omitter.setVisibility(View.GONE);
                        skip.setText(getString(R.string.continuar));
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
                int position = viewPager.getCurrentItem();
                if(position>0) {
                    viewPager.setCurrentItem(position - 1);
                }
            });

            skip.setOnClickListener(view -> {
                int position = viewPager.getCurrentItem();
                if(position<fragments_data.length-1) {
                    viewPager.setCurrentItem(position +1);
                }else if(position ==2){
                    Intent i = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(i);
                    App.write(IS_INTRO_COMPLETED,true);
                    finish();
                }else{
                    Intent i = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(i);
                    App.write(IS_INTRO_COMPLETED,true);
                    finish();
                }

            });



    }

    void move_pager_up(){
        indicators.setPadding(0
                ,0
                ,0
                , Utilities.convertDpToPixel(70f,this));
    }
    void move_pager_down(){
        indicators.setPadding( 0,0,0,0);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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

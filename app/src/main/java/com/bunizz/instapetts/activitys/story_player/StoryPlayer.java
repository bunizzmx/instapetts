package com.bunizz.instapetts.activitys.story_player;

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
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.story.FragmentStoriView;
import com.bunizz.instapetts.listeners.story_finished_listener;
import com.bunizz.instapetts.utils.ViewPagerHistory.DepthPageTransformer;
import com.bunizz.instapetts.utils.tabs.SlidingFragmentPagerAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoryPlayer extends AppCompatActivity implements story_finished_listener,StoryPlayerContract.View {
    public static final String STORY_IMAGE_KEY = "storyImages";
    @BindView(R.id.view_pager_stories)
    ViewPager view_pager_stories;
    private TabAdapter adapter;
    int CURRENT_ITEM =0;
    int SELECTED_POSITION =0;
    ArrayList<HistoriesBean> historiesBeans = new ArrayList<>();
    StoryPlapyerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_activity);
        ButterKnife.bind(this);
        changeStatusBarColor(R.color.black);
        presenter = new StoryPlapyerPresenter(this,this);
        if (getIntent() != null) {
            historiesBeans = Parcels.unwrap(getIntent().getParcelableExtra("sliders"));
            if(historiesBeans.get(0).getHistorias() == null)
                historiesBeans.remove(0);
            SELECTED_POSITION = getIntent().getIntExtra("SELECTED_POSITION",0);
        }
        Log.e("STORY_PLAYER","-->:Num Histories: " + historiesBeans.size());
        Log.e("STORY_PLAYER","-->:Num position: " + SELECTED_POSITION);
        adapter = new TabAdapter(getSupportFragmentManager(), this);
        for(int i =0;i<historiesBeans.size();i++){
            Bundle bundle = new Bundle();
            bundle.putParcelable("HISTORY_PARAMETER", Parcels.wrap(this.historiesBeans.get(i)));
            Log.e("AUTPLAY_SELECTED","-->" + SELECTED_POSITION + "/" +i);
            if(SELECTED_POSITION == i)
            bundle.putBoolean("AUTOPLAY",true);
            else
            bundle.putBoolean("AUTOPLAY",true);
            Fragment f = new FragmentStoriView();
            f.setArguments(bundle);
            adapter.addFragment(f);
        }
        CURRENT_ITEM = SELECTED_POSITION;
        view_pager_stories.setAdapter(adapter);
        view_pager_stories.setOffscreenPageLimit(0);
        view_pager_stories.setPageTransformer(true, new DepthPageTransformer());
        view_pager_stories.setCurrentItem(SELECTED_POSITION);
        view_pager_stories.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CURRENT_ITEM = position;
                ((FragmentStoriView) adapter.getItem(CURRENT_ITEM)).startProgressAnimation();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(CURRENT_ITEM!=0)
            ((FragmentStoriView) adapter.getItem(CURRENT_ITEM - 1)).StopProgressAnimation();
        else
            ((FragmentStoriView) adapter.getItem(CURRENT_ITEM )).StopProgressAnimation();

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
        if(!(CURRENT_ITEM + 1 >= adapter.getCount())){
            Log.e("PAGER_HISTORIES","LA QUE SIGUE");
            if(SELECTED_POSITION != historiesBeans.size()-1) {
                Log.e("PAGER_HISTORIES","la que sigue porque no es la ultima :" +(CURRENT_ITEM + 1));
                view_pager_stories.setCurrentItem(CURRENT_ITEM + 1);
            }
            else {
                Log.e("PAGER_HISTORIES","es la ultima lo termino");
                finish();
            }
        }else{
            Log.e("PAGER_HISTORIES","a la verga");
            finish();
        }

    }

    @Override
    public void onItemView(String identificador, int id_usuario) {
        presenter.ViewHistory(identificador,id_usuario);
    }

    @Override
    public void onItemLiked(String identificador, int id_usuario) {
       presenter.LikeHistory(identificador,id_usuario);
    }

    @Override
    public void onItemDeleted(String history) {
       presenter.deleteMyHistory(history);
    }

    @Override
    public IdentificadoresHistoriesBean getIdenTificador(String identificador) {
        return presenter.getIdentificador(identificador);
    }

    @Override
    public void show_feed(ArrayList<PostBean> data, ArrayList<HistoriesBean> data_stories) {

    }


    public class TabAdapter extends SlidingFragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private Context context;

        public TabAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


    }



}

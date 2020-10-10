package com.bunizz.instapetts.fragments.vertical_videos;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.vertical_videos.fragments.playVideoFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewPagerVideoFragment extends Fragment  {

    @BindView(R.id.view_pager_videos)
    ViewPager view_pager_videos;

    @BindView(R.id.adView)
     AdView mAdView;

    @BindView(R.id.tab_seguidos)
    TextView tab_seguidos;

    @BindView(R.id.tab_para_ti)
    TextView tab_para_ti;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_seguidos)
    void tab_seguidos() {
        change_tab_seguidos();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_para_ti)
    void tab_para_ti() {
       change_tab_para_ti();
    }

    ViewPagerAdapter adapter_pager;
    public static ViewPagerVideoFragment newInstance() {
        return new ViewPagerVideoFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter_pager = new ViewPagerAdapter(getChildFragmentManager());
        adapter_pager.addFragment(new playVideoFragment(), getString(R.string.pager_discover));
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.viewpager_video_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        view_pager_videos.setAdapter(adapter_pager);
        view_pager_videos.setOffscreenPageLimit(1);
        view_pager_videos.setOnTouchListener((view1, motionEvent) -> false);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        change_tab_para_ti();
    }


    // An equivalent ViewPager2 adapter class
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new playVideoItem();
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public void change_tab_para_ti(){
        tab_para_ti.setTextSize(18);
        tab_para_ti.setTextColor(getActivity().getResources().getColor(R.color.primary));
        tab_seguidos.setTextSize(12);
        tab_seguidos.setTextColor(Color.WHITE);
    }

    public void change_tab_seguidos(){
        tab_para_ti.setTextSize(12);
        tab_para_ti.setTextColor(Color.WHITE);
        tab_seguidos.setTextSize(18);
        tab_seguidos.setTextColor(getActivity().getResources().getColor(R.color.primary));
    }


    @Override
    public void onPause() {
        Log.e("ESTATUS_PARENT_F","onPause");
        super.onPause();
        ((playVideoFragment)adapter_pager.getItem(0)).stopVideos();
    }

    public void stop_videos(){
        ((playVideoFragment)adapter_pager.getItem(0)).stopVideos();
    }

    public void reanudarPLayers(){
        Log.e("ESTATUS_ACTIVITY","release players");
        if(adapter_pager!=null)
            ((playVideoFragment)adapter_pager.getItem(0)).reanudarPLayers();
    }

}

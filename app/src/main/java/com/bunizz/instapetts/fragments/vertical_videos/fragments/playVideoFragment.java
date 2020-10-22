package com.bunizz.instapetts.fragments.vertical_videos.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PlayVideos;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.vertical_videos.playVideoContract;
import com.bunizz.instapetts.fragments.vertical_videos.playVideoItem;
import com.bunizz.instapetts.fragments.vertical_videos.playVideoPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;

public class playVideoFragment extends Fragment implements playVideoContract.View {

    @BindView(R.id.view_pager_stories)
    ViewPager2 view_pager_stories;

    @BindView(R.id.refresh_videos)
    SwipeRefreshLayout refresh_videos;



    ScreenSlidePagerAdapter adapter;
    playVideoPresenter presenter;
    ArrayList<PlayVideos>  videos =new ArrayList<>();
    ArrayList<PlayVideos>  playVideos =new ArrayList<>();
    int CURRENT_POSITION =0;
    int PAGINADOR = -999;

    public static playVideoFragment newInstance() {
        return new playVideoFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ScreenSlidePagerAdapter(getActivity());
        presenter = new playVideoPresenter(this,getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_viewpager_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        view_pager_stories.setAdapter(adapter);
        presenter.getVideos(-999,false,0);
        view_pager_stories.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                CURRENT_POSITION = position;
                if(videos.size() - 2 > 0) {
                    if (position > videos.size() - 2) {
                        presenter.getVideos(PAGINADOR,true,0);
                        Log.e("DEBO_PEDIR_MAS", "SI : " + PAGINADOR);
                    }else{
                        Log.e("DEBO_PEDIR_MAS", "AUN NO");
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        refresh_videos.setOnRefreshListener(() -> {
            PAGINADOR= -999;
            presenter.getVideos(PAGINADOR,false,0);
        });
    }

    @Override
    public void showVideos(ArrayList<PlayVideos> data) {
        refresh_videos.setRefreshing(false);
        videos.clear();
        for (int i =0;i<data.size();i++){
            PAGINADOR = data.get(i).getId()
;            if(presenter.is_video_liked(data.get(i).getId())){
                data.get(i).setLiked(true);
            }else{
                data.get(i).setLiked(false);
            }
        }
        videos.addAll(data);
        adapter.setPlayVideos(videos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMoreVideos(ArrayList<PlayVideos> data) {
        videos.clear();
        for (int i =0;i<data.size();i++){
            PAGINADOR = data.get(i).getId()
            ;            if(presenter.is_video_liked(data.get(i).getId())){
                data.get(i).setLiked(true);
            }else{
                data.get(i).setLiked(false);
            }
        }
        videos.addAll(data);
        adapter.addMoreVideos(videos);
        adapter.notifyDataSetChanged();
    }

    public void stopVideos(){
          adapter.stop_videos();
    }



    // An equivalent ViewPager2 adapter class
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        ArrayList<PlayVideos>  videos =new ArrayList<>();
        Fragment currentfragment = null;

        public void setPlayVideos(ArrayList<PlayVideos> playVideos) {
            this.videos.clear();
            this.videos.addAll(playVideos);
        }

        public void addMoreVideos(ArrayList<PlayVideos> playVideos) {
            this.videos.addAll(playVideos);
        }



        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            Log.e("CURRENT_POSITION","->" + position);
            mFragmentList.add(playVideoItem.newInstancex(videos.get(position)));
            return mFragmentList.get(position);
        }

        public void stop_videos(){
            if(mFragmentList!=null) {
                if(mFragmentList.size()>0)
                   ((playVideoItem) mFragmentList.get(CURRENT_POSITION)).stopPlayers();
            }
        }

        public void reanudar(){
            if(mFragmentList!=null) {
                if(mFragmentList.size()>0)
                   ((playVideoItem) mFragmentList.get(CURRENT_POSITION)).reanudarPLayers();
            }
        }
        @Override
        public int getItemCount() {
            return videos.size();
        }
    }
    public void reanudarPLayers(){
        if(adapter!= null)
            adapter.reanudar();
    }

}

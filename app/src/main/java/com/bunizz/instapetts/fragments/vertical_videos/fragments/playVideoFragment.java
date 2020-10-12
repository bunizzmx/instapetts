package com.bunizz.instapetts.fragments.vertical_videos.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PlayVideos;
import com.bunizz.instapetts.fragments.vertical_videos.playVideoContract;
import com.bunizz.instapetts.fragments.vertical_videos.playVideoItem;
import com.bunizz.instapetts.fragments.vertical_videos.playVideoPresenter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;

public class playVideoFragment extends Fragment implements playVideoContract.View {

    @BindView(R.id.view_pager_stories)
    ViewPager2 view_pager_stories;
    ScreenSlidePagerAdapter adapter;
    playVideoPresenter presenter;
    ArrayList<PlayVideos>  videos =new ArrayList<>();
    ArrayList<PlayVideos>  playVideos =new ArrayList<>();
    int CURRENT_POSITION =0;

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
                        presenter.getVideos(1,true,0);
                        Log.e("DEBO_PEDIR_MAS", "SI");
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

    }

    @Override
    public void showVideos(ArrayList<PlayVideos> data) {
        videos.clear();
        for (int i =0;i<data.size();i++){
            if(presenter.is_video_liked(data.get(i).getId())){
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

    }

    public void stopVideos(){
          adapter.stop_videos();
    }



    // An equivalent ViewPager2 adapter class
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        ArrayList<PlayVideos>  videos =new ArrayList<>();
        Fragment currentfragment = null;

        public void setPlayVideos(ArrayList<PlayVideos> playVideos) {
            this.videos.clear();
            this.videos.addAll(playVideos);
        }

        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
             currentfragment =  playVideoItem.newInstancex(videos.get(position));
            return currentfragment;
        }

        public void stop_videos(){
            if(currentfragment!=null)
                ((playVideoItem )currentfragment).stopPlayers();
        }

        public void reanudar(){
            if(currentfragment!=null)
               ((playVideoItem )currentfragment).reanudarPLayers();
        }
        @Override
        public int getItemCount() {
            return videos.size();
        }
    }
    public void reanudarPLayers(){
        Log.e("ESTATUS_ACTIVITY","release players");
        if(adapter!= null)
           adapter.reanudar();
    }

}

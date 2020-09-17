package com.bunizz.instapetts.fragments.vertical_videos.fragments;

import android.os.Bundle;
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

    ArrayList<PlayVideos>  playVideos =new ArrayList<>();

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
        presenter.getVideos(false,0);

    }

    @Override
    public void showVideos(ArrayList<PlayVideos> data) {
        adapter.setPlayVideos(data);
        adapter.notifyDataSetChanged();
    }


    // An equivalent ViewPager2 adapter class
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        ArrayList<PlayVideos>  videos =new ArrayList<>();

        public ArrayList<PlayVideos> getPlayVideos() {
            return videos;
        }

        public void setPlayVideos(ArrayList<PlayVideos> playVideos) {
            this.videos = playVideos;
        }

        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return  playVideoItem.newInstancex(videos.get(position));
        }

        @Override
        public int getItemCount() {
            return videos.size();
        }
    }


}

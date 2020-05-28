package com.bunizz.instapetts.fragments.story;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.story_player.StoryPlayer;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.previewProfile.FragmentProfileUserPetPreview;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.story_finished_listener;
import com.bunizz.instapetts.utils.ViewPagerHistory.DepthPageTransformer;
import com.bunizz.instapetts.utils.tabs.SlidingFragmentPagerAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StoryPlayerFragment extends Fragment {

    public static final String STORY_IMAGE_KEY = "storyImages";
    @BindView(R.id.view_pager_stories)
    ViewPager view_pager_stories;
    private TabAdapter adapter;
    int CURRENT_ITEM =0;
    int SELECTED_POSITION =0;
    ArrayList<HistoriesBean> historiesBeans = new ArrayList<>();


    changue_fragment_parameters_listener listener;

    public static StoryPlayerFragment newInstance() {
        return new StoryPlayerFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.story_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            historiesBeans = Parcels.unwrap(b.getParcelable("sliders"));
            SELECTED_POSITION = b.getInt("SELECTED_POSITION",0);
        }
        Log.e("STORY_PLAYER","-->:Num Histories: " + historiesBeans.size());
        Log.e("STORY_PLAYER","-->:Num position: " + SELECTED_POSITION);
        adapter = new TabAdapter(getChildFragmentManager(), getContext());
        for(int i =0;i<historiesBeans.size();i++){
            Bundle bundle = new Bundle();
            bundle.putParcelable("HISTORY_PARAMETER", Parcels.wrap(this.historiesBeans.get(i)));
            Log.e("AUTPLAY_SELECTED","-->" + SELECTED_POSITION + "/" +i);
            if(SELECTED_POSITION == i)
                bundle.putBoolean("AUTOPLAY",true);
            else
                bundle.putBoolean("AUTOPLAY",false);
            Fragment f = new FragmentStoriView();
            f.setArguments(bundle);
            adapter.addFragment(f);
        }
        CURRENT_ITEM = SELECTED_POSITION;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
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


    public void setCurrentItem(){
        if(!(CURRENT_ITEM + 1 >= adapter.getCount())){

            Log.e("PAGER_HISTORIES","LA QUE SIGUE");
            if(SELECTED_POSITION != historiesBeans.size()-1) {
                Log.e("PAGER_HISTORIES","la que sigue porque no es la ultima :" +(CURRENT_ITEM + 1));
                view_pager_stories.setCurrentItem(CURRENT_ITEM + 1);
            }
            else {
                Log.e("PAGER_HISTORIES","es la ultima lo termino");
                getActivity().finish();
            }
        }else{
            Log.e("PAGER_HISTORIES","a la verga");
            getActivity().finish();
        }
    }

    public  void onPause() {
        super.onPause();
        if(CURRENT_ITEM!=0)
            ((FragmentStoriView) adapter.getItem(CURRENT_ITEM - 1)).StopProgressAnimation();
        else
            ((FragmentStoriView) adapter.getItem(CURRENT_ITEM )).StopProgressAnimation();

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener =(changue_fragment_parameters_listener) context;
    }
}

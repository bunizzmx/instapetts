package com.bunizz.instapetts.fragments.share_post;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.camera.CameraFragment;
import com.bunizz.instapetts.fragments.camera.CameraFragmentKt;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.post.FragmentPostList;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.search.tabs.FragmentPetList;
import com.bunizz.instapetts.fragments.search.tabs.FragmentPopietaryList;
import com.bunizz.instapetts.fragments.share_post.Picker.FragmentPickerGalery;
import com.bunizz.instapetts.fragments.share_post.Share.FragmentSharePost;
import com.bunizz.instapetts.fragments.share_post.Share.ListSelectedAdapter;
import com.bunizz.instapetts.fragments.share_post.Share.SharePostContract;
import com.bunizz.instapetts.fragments.share_post.Share.SharePostPresenter;
import com.bunizz.instapetts.utils.dilogs.DialogShosePet;
import com.bunizz.instapetts.utils.tabs.SlidingFragmentPagerAdapter;
import com.bunizz.instapetts.utils.tabs.SlidingTabLayout;
import com.bunizz.instapetts.utils.tabs.TabType;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContainerFragmentsShare extends Fragment{


    @BindView(R.id.tabs_camera)
    SlidingTabLayout tabs_camera;

    @BindView(R.id.viewpager_camera)
    ViewPager viewpager_camera;

    private TabAdapter adapter;
    int is_from_profile=0;

    public static ContainerFragmentsShare newInstance() {
        return new ContainerFragmentsShare();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TabAdapter(getFragmentManager(), getContext());
        Bundle bundle=getArguments();
        if(bundle!=null){
            is_from_profile = bundle.getInt("FROM_PROFILE");
            Log.e("FROM_PROFILE","--->yyyyy" + is_from_profile);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_containers_cameras, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        viewpager_camera.setAdapter(adapter);
        tabs_camera.setTabType(TabType.TEXT_ONLY);
        tabs_camera.setViewPager(viewpager_camera);
        tabs_camera.setTextSize(18);
        tabs_camera.setDistributeEvenly(true);
        tabs_camera.setCustomUnfocusedColor(R.color.black);
        tabs_camera.setSelectedIndicatorColors(getResources().getColor(R.color.naranja));
    }

    public class TabAdapter extends SlidingFragmentPagerAdapter {

        private String[] titles = {
                "Galeria",
                "Foto"
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
            bundle.putInt("FROM_PROFILE",is_from_profile);
            Log.e("FROM_PROFILE","--->xxxxxx" + is_from_profile);
            Fragment fragment;
            if(position == 0){
                fragment = new FragmentPickerGalery();
            }else{
                fragment = new CameraFragment();
            }

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return  titles.length ;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }



        @Override
        public String getToolbarTitle(int position) {
            return titles[position];
        }
    }
}

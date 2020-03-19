package com.bunizz.instapetts.fragments.search;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.activitys.searchqr.QrSearchActivity;
import com.bunizz.instapetts.activitys.share_post.ShareActivity;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.post.FragmentPostList;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.search.tabs.FragmentPetList;
import com.bunizz.instapetts.fragments.search.tabs.FragmentPopietaryList;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.utils.tabs.SlidingFragmentPagerAdapter;
import com.bunizz.instapetts.utils.tabs.SlidingTabLayout;
import com.bunizz.instapetts.utils.tabs.TabType;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSearchPet extends Fragment {


    change_instance listener;

    @BindView(R.id.searching_results_list_pets)
    RecyclerView searching_results_list_pets;

    SearchPetAdapter adapter;

    @BindView(R.id.tabs_search)
    SlidingTabLayout tabs_search;

    @BindView(R.id.viewpager_search)
    ViewPager viewpager_search;
    TabAdapter tabAdapter;

    @BindView(R.id.search_by_qr)
    RelativeLayout search_by_qr;
    private static final int REQUEST_CODE_QR_SCAN = 101;

 RxPermissions rxPermissions;
    public static FragmentSearchPet newInstance() {
        return new FragmentSearchPet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabAdapter = new TabAdapter(getFragmentManager(), getContext());
        rxPermissions = new RxPermissions(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_searching_pet, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        adapter = new SearchPetAdapter(getContext());
        searching_results_list_pets.setLayoutManager(new LinearLayoutManager(getContext()));
        searching_results_list_pets.setAdapter(adapter);
        viewpager_search.setAdapter(tabAdapter);
        tabs_search.setTabType(TabType.TEXT_ONLY);
        tabs_search.setViewPager(viewpager_search);
        tabs_search.setDistributeEvenly(true);
        tabs_search.setCustomUnfocusedColor(R.color.black);
        tabs_search.setSelectedIndicatorColors(getResources().getColor(R.color.amarillo));
        search_by_qr.setOnClickListener(view1 -> {
            rxPermissions
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA)
                    .subscribe(granted -> {
                        if (granted) {
                            Intent i = new Intent(getContext() , QrSearchActivity.class);
                            startActivityForResult( i,REQUEST_CODE_QR_SCAN);
                        } else {
                            App.getInstance().show_dialog_permision(getActivity(),getActivity().getResources().getString(R.string.permision_storage),
                                    getResources().getString(R.string.permision_storage_body),0);
                        }
                    });

        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
    }

    public class TabAdapter extends SlidingFragmentPagerAdapter {

        private String[] titles = {
                "Propietarios",
                "Mascotas",
                "Recientes",
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
            if(position == 0){
                fragment = new FragmentPopietaryList();
            }else{
                fragment = new FragmentPetList();
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


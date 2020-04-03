package com.bunizz.instapetts.fragments.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.SearchPetBean;
import com.bunizz.instapetts.beans.SearchUserBean;
import com.bunizz.instapetts.fragments.search.tabs.pets.FragmentPetList;
import com.bunizz.instapetts.fragments.search.tabs.users.FragmentPopietaryList;
import com.bunizz.instapetts.fragments.search.tabs.pets.SearchPetAdapter;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.tabs.SlidingFragmentPagerAdapter;
import com.bunizz.instapetts.utils.tabs.SlidingTabLayout;
import com.bunizz.instapetts.utils.tabs.TabType;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentSearchPet extends Fragment implements SearchPetContract.View{


    changue_fragment_parameters_listener listener;

    @BindView(R.id.searching_results_list_pets)
    RecyclerView searching_results_list_pets;

    SearchPetAdapter adapter;

    @BindView(R.id.tabs_search)
    SlidingTabLayout tabs_search;

    @BindView(R.id.viewpager_search)
    ViewPager viewpager_search;
    TabAdapter tabAdapter;

    @BindView(R.id.search_input)
    EditText search_input;

    SearchPetPresenter presenter;
    int POSITION_PAGER =0;

    String QUERY_PETS_SAVED="";
    String QUERY_USERS_SAVED="";

    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
    Runnable workRunnable;
 RxPermissions rxPermissions;
    public static FragmentSearchPet newInstance() {
        return new FragmentSearchPet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabAdapter = new TabAdapter(getFragmentManager(), getContext());
        rxPermissions = new RxPermissions(getActivity());
        presenter = new SearchPetPresenter(this,getContext());
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
        adapter.setListener(new changue_fragment_parameters_listener() {
            @Override
            public void change_fragment_parameter(int type_fragment, Bundle data) {
                if(listener!=null)
                    listener.change_fragment_parameter(type_fragment,data);
            }
        });
        viewpager_search.setAdapter(tabAdapter);
        tabs_search.setTabType(TabType.TEXT_ONLY);
        tabs_search.setViewPager(viewpager_search);
        tabs_search.setDistributeEvenly(true);
        tabs_search.setCustomUnfocusedColor(R.color.black);
        tabs_search.setSelectedIndicatorColors(getResources().getColor(R.color.primary));
        search_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()> 3){
                    handler.removeCallbacks(workRunnable);
                    workRunnable = () ->{
                        if(POSITION_PAGER == 0)
                           presenter.searchusers(search_input.getText().toString());
                        else
                            presenter.searchPets(search_input.getText().toString());
                    };
                    handler.postDelayed(workRunnable, 1000 /*delay*/);
                }
            }
        });

        viewpager_search.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position>0){
                   // QUERY_PETS_SAVED = search_input.getText().toString();
                    search_input.setText("");
                }else{
                    //QUERY_USERS_SAVED = search_input.getText().toString();
                    search_input.setText("");
                }
                POSITION_PAGER = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }

    @Override
    public void shoPetsResults(ArrayList<SearchPetBean> pets) {
        Fragment frag = tabAdapter.fragments[POSITION_PAGER];
        if (frag instanceof FragmentPetList) {
            ArrayList<Object> results = new ArrayList<>();
            results.addAll(pets);
            ((FragmentPetList) frag).setData(results);
        }
    }

    @Override
    public void shoUsersResults(ArrayList<SearchUserBean> users) {
        Fragment frag = tabAdapter.fragments[POSITION_PAGER];
        if (frag instanceof FragmentPopietaryList) {
            ArrayList<Object> results = new ArrayList<>();
            results.addAll(users);
            ((FragmentPopietaryList) frag).setData(results);
        }
    }

    public class TabAdapter extends SlidingFragmentPagerAdapter {

        private String[] titles = {
               getContext().getResources().getString(R.string.users),
                getContext().getResources().getString(R.string.pets)
        };
        public Fragment[] fragments = new Fragment[titles.length];
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
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            fragments[position]  = createdFragment;
            return createdFragment;
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


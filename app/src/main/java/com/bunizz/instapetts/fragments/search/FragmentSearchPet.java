package com.bunizz.instapetts.fragments.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.SearchPetBean;
import com.bunizz.instapetts.beans.SearchUserBean;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.search.tabs.pets.FragmentPetList;
import com.bunizz.instapetts.fragments.search.tabs.users.FragmentPopietaryList;
import com.bunizz.instapetts.fragments.search.tabs.pets.SearchPetAdapter;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.tabs.SlidingFragmentPagerAdapter;
import com.bunizz.instapetts.utils.tabs.SlidingTabLayout;
import com.bunizz.instapetts.utils.tabs.TabType;
import com.bunizz.instapetts.utils.tabs2.SmartTabLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class FragmentSearchPet extends Fragment implements SearchPetContract.View{


    changue_fragment_parameters_listener listener;

    @BindView(R.id.viewpager_search)
    ViewPager viewpager_search;

    @BindView(R.id.search_input)
    EditText search_input;

    @BindView(R.id.tabs_search)
    SmartTabLayout tabs_search;

    @BindView(R.id.searching_world)
    ProgressBar searching_world;


    SearchPetPresenter presenter;
    int POSITION_PAGER =0;

    String QUERY_PETS_SAVED="";
    String QUERY_USERS_SAVED="";
    ViewPagerAdapterx adapter_pager;

    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
    Runnable workRunnable;
 RxPermissions rxPermissions;
    public static FragmentSearchPet newInstance() {
        return new FragmentSearchPet();
    }
    private Handler mHandler= new Handler();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = new RxPermissions(getActivity());
        presenter = new SearchPetPresenter(this,getContext());
        Log.e("CRE_FRAGMENT","ONCREATE SERACH");
        adapter_pager = new ViewPagerAdapterx(getChildFragmentManager());
        adapter_pager.addFragment(new FragmentPopietaryList(), "Usuarios");
        adapter_pager.addFragment(new FragmentPetList(), "Mascotas");
        adapter_pager.addFragment(new FragmentPetList(), "Lugares");
        adapter_pager.addFragment(new FragmentPetList(), "Cerca de ti");
        adapter_pager.addFragment(new FragmentPetList(), "Lo más reciente");

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
        viewpager_search.setAdapter(adapter_pager);
        tabs_search.setViewPager(viewpager_search);
        search_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searching_world.setVisibility(View.VISIBLE);
                if(s.length()> 3){
                    handler.removeCallbacks(workRunnable);
                    workRunnable = () ->{
                        if(POSITION_PAGER == 0)
                           presenter.searchusers(search_input.getText().toString());
                        else
                            presenter.searchPets(search_input.getText().toString());
                    };
                    handler.postDelayed(workRunnable, 1000 /*delay*/);
                }else if(s.toString().length() == 0 ){
                    Log.e("SHOW_RECENT","SI");
                    Fragment frag = adapter_pager.getItem(POSITION_PAGER);
                    if(frag instanceof FragmentPetList ){
                        ((FragmentPetList) frag).showRecent();
                    }else{
                        ((FragmentPopietaryList) frag).showRecent();
                    }
                    searching_world.setVisibility(View.GONE);
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
        mHandler.post(() -> {
                    InputMethodManager inputMethodManager =  (InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(search_input.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                    search_input.requestFocus();
                });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }

    @Override
    public void shoPetsResults(ArrayList<SearchPetBean> pets) {
        searching_world.setVisibility(View.GONE);
        Fragment frag = adapter_pager.getItem(1);
        if (frag instanceof FragmentPetList) {
            Log.e("SHOW_RESULT_USER","PEST");
            ArrayList<Object> results = new ArrayList<>();
            results.addAll(pets);
            ((FragmentPetList) frag).setData(results);
        }
    }

    @Override
    public void shoUsersResults(ArrayList<SearchUserBean> users) {
        searching_world.setVisibility(View.GONE);
        Fragment frag =  adapter_pager.getItem(0);
        if (frag instanceof FragmentPopietaryList) {
            Log.e("SHOW_RESULT_USER","USUARIOS");
            ArrayList<Object> results = new ArrayList<>();
            results.addAll(users);
            ((FragmentPopietaryList) frag).setData(results);
        }
    }



    class ViewPagerAdapterx extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapterx(FragmentManager manager) {
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

    @Override
    public void onResume() {
        super.onResume();

    }
}


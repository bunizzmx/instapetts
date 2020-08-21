package com.bunizz.instapetts.fragments.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
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

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.SearchPetBean;
import com.bunizz.instapetts.beans.SearchUserBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.search.tabs.pets.FragmentPetList;
import com.bunizz.instapetts.fragments.search.tabs.users.FragmentPopietaryList;
import com.bunizz.instapetts.fragments.search.tabs.pets.SearchPetAdapter;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.bottom_sheet.SlidingUpPanelLayout;
import com.bunizz.instapetts.utils.tabs.SlidingFragmentPagerAdapter;
import com.bunizz.instapetts.utils.tabs.SlidingTabLayout;
import com.bunizz.instapetts.utils.tabs.TabType;
import com.bunizz.instapetts.utils.tabs2.SmartTabLayout;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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

    @SuppressLint("MissingPermission")
    @OnClick(R.id.icon_search)
    void icon_search() {
       getActivity().onBackPressed();
    }



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
        adapter_pager = new ViewPagerAdapterx(getChildFragmentManager());
        adapter_pager.addFragment(new FragmentPopietaryList(), getContext().getString(R.string.users));
        adapter_pager.addFragment(new FragmentPetList(), getContext().getString(R.string.pets));
       // adapter_pager.addFragment(new FragmentPetList(), getContext().getString(R.string.cerca_De_ti));
        adapter_pager.addFragment(new FragmentPopietaryList(),  getContext().getString(R.string.users_news));

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
        viewpager_search.setOffscreenPageLimit(3);

        RxTextView.textChanges(search_input)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    searching_world.setVisibility(View.VISIBLE);
                    if(charSequence.toString().trim().length()> 3){
                        handler.removeCallbacks(workRunnable);
                        workRunnable = () ->{
                            if(POSITION_PAGER == 0)
                                presenter.searchusers(search_input.getText().toString());
                            else
                                presenter.searchPets(search_input.getText().toString());
                        };
                        handler.postDelayed(workRunnable, 1000);
                    }else if(charSequence.toString().trim().length() == 0 ){
                        Fragment frag = adapter_pager.getItem(POSITION_PAGER);
                        if(POSITION_PAGER!=2) {
                            if (frag instanceof FragmentPetList)
                                ((FragmentPetList) frag).showRecent();
                            else
                                ((FragmentPopietaryList) frag).showRecent();

                        }
                        searching_world.setVisibility(View.GONE);
                    }
                });



        viewpager_search.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position>0)
                    search_input.setText("");
                else
                    search_input.setText("");

                POSITION_PAGER = position;
                if(position == 2){
                    presenter.searchNewUsers();
                }
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
        Fragment frag = adapter_pager.getItem(POSITION_PAGER);
        ArrayList<Object> PETS = new ArrayList<>();
        if(pets!=null) {
            if (frag instanceof FragmentPetList) {
                PETS.clear();
                Log.e("SHOW_RESULT_USER", "PEST");
                for (int i = 0; i < pets.size(); i++) {
                    if (pets.get(i).getId_user() != App.read(PREFERENCES.ID_USER_FROM_WEB, 0)) {
                        PETS.add(pets.get(i));
                    }
                }
                ((FragmentPetList) frag).setData(PETS);
            }
        }
    }

    @Override
    public void shoUsersResults(ArrayList<SearchUserBean> users) {
        searching_world.setVisibility(View.GONE);
        Fragment frag =  adapter_pager.getItem(POSITION_PAGER);
        ArrayList<Object> USERS = new ArrayList<>();
        if (frag instanceof FragmentPopietaryList) {
            Log.e("SHOW_RESULT_USER","USUARIOS");
            USERS.clear();
            for (int i =0 ;i<users.size();i++){
                if(Integer.parseInt(users.get(i).getId_user())!= App.read(PREFERENCES.ID_USER_FROM_WEB,0)){
                    USERS.add(users.get(i));
                }
            }
            ((FragmentPopietaryList) frag).setData(USERS);
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


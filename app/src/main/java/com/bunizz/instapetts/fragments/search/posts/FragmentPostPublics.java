package com.bunizz.instapetts.fragments.search.posts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.searchqr.QrSearchActivity;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.previewProfile.FragmentProfileUserPetPreview;
import com.bunizz.instapetts.fragments.search.AdapterGridPosts;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.tabs2.SmartTabLayout;
import com.bunizz.instapetts.web.CONST;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentPostPublics  extends Fragment implements  PostPublicsContract.View {




    ArrayList<Object> data_posts = new ArrayList<>();


    @BindView(R.id.root_no_internet)
    RelativeLayout root_no_internet;

    @BindView(R.id.tabs_profile_propietary)
    SmartTabLayout tabs_profile_propietary;

    @BindView(R.id.viewpager_search)
    ViewPager viewpager_profile;

    @BindView(R.id.refresh_search)
    SwipeRefreshLayout refresh_search;

    ViewPagerAdapter adapter_pager;
    @SuppressLint("MissingPermission")
    @OnClick(R.id.root_search_pets_users)
    void root_search_pets_users()
    {
      listener.change_fragment_parameter(FragmentElement.INSTANCE_SEARCH,null);
    }

    String URL_UPDATED="INVALID";
    String URL_LOCAL="INVALID";


    PostPublicsPresenter presenter;
    int IS_FORM_SAVED_POST =0;
    @BindView(R.id.search_by_qr)
    RelativeLayout search_by_qr;
    private static final int REQUEST_CODE_QR_SCAN = 101;

    changue_fragment_parameters_listener listener;
    RxPermissions rxPermissions;
    public static FragmentPostPublics newInstance() {
        return new FragmentPostPublics();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            IS_FORM_SAVED_POST = bundle.getInt("SAVED_POST");
        }
        adapter_pager = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter_pager.addFragment(new FragmentListGalery(), getString(R.string.pager_discover));
        adapter_pager.addFragment(new FragmentListGalery(), getString(R.string.pager_more_views));
        adapter_pager.addFragment(new FragmentListGalery(), getString(R.string.pager_adorable));
        adapter_pager.addFragment(new FragmentListGalery(), getString(R.string.pager_recent));

        presenter = new PostPublicsPresenter(this,getContext());
        rxPermissions = new RxPermissions(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_publics, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        presenter.getPostPublics();
        search_by_qr.setOnClickListener(view1 -> {
            rxPermissions
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA)
                    .subscribe(granted -> {
                        if (granted) {
                            listener.change_fragment_parameter(999,null);
                        } else {
                            App.getInstance().show_dialog_permision(getActivity(),getActivity().getResources().getString(R.string.permision_storage),
                                    getResources().getString(R.string.permision_storage_body),0);
                        }
                    });

        });
        viewpager_profile.setAdapter(adapter_pager);
        tabs_profile_propietary.setViewPager(viewpager_profile);
        refresh_search.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getPostPublics();
            }
        });
    }




    @Override
    public void showPosts(ArrayList<PostBean> posts) {
        refresh_search.setRefreshing(false);
        data_posts.clear();
        data_posts.addAll(posts);
        Fragment frag = adapter_pager.getItem(0);
        if (frag instanceof FragmentListGalery) {
            ((FragmentListGalery) frag).setData_posts(data_posts);
        }
    }

    @Override
    public void noInternet() {
         root_no_internet.setVisibility(View.VISIBLE);
    }

    @Override
    public void peticionError() {
       presenter.getPostPublics();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
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
}
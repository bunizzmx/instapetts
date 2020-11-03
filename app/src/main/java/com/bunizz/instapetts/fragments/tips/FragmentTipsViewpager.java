package com.bunizz.instapetts.fragments.tips;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.TipsBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedViewPager;
import com.bunizz.instapetts.fragments.feed.fragments.Feed;
import com.bunizz.instapetts.fragments.feed.fragments.FeedParaTi;
import com.bunizz.instapetts.fragments.tips.adapters.TipsAdapter;
import com.bunizz.instapetts.fragments.tips.fragments.Tips;
import com.bunizz.instapetts.fragments.tips.fragments.TipsforMyPets;
import com.bunizz.instapetts.listeners.PlayStopVideoListener;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.conexion_listener;
import com.bunizz.instapetts.utils.AnimatedTextViews.TyperTextView;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;
import com.bunizz.instapetts.utils.smoot.SmoothProgressBar;
import com.bunizz.instapetts.utils.video_player.ExoPlayerRecyclerViewTips;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentTipsViewpager extends Fragment implements  TipsContract.View {

    change_instance listener;
    changue_fragment_parameters_listener listener_change;
    TipsAdapter adapter;
    TipsPresenter presenter;
    ArrayList<Object> data = new ArrayList<>();
    conexion_listener listener_wifi;

    @BindView(R.id.view_pager_tips)
    ViewPager view_pager_tips;

    @BindView(R.id.tab_seguidos)
    TextView tab_seguidos;

    @BindView(R.id.tab_para_ti)
    TextView tab_para_ti;

    ViewPagerAdapter adapter_pager;
    private boolean loading =true;
    private boolean IS_ALL = false;
    int PAGINADOR = -999;



    @OnClick(R.id.tab_para_ti)
    void tab_para_ti()
    {
        view_pager_tips.setCurrentItem(1);
        change_tab_para_ti();
    }
    @OnClick(R.id.tab_seguidos)
    void tab_seguidos()
    {
        view_pager_tips.setCurrentItem(0);
        change_tab_seguidos();
    }

    @OnClick(R.id.open_retos_eventos)
    void open_retos_eventos()
    {
        listener.change(FragmentElement.INSTANCE_EVENTOS);
    }

    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    public static FragmentTipsViewpager newInstance() {
        return new FragmentTipsViewpager();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TipsAdapter(getContext());
        adapter.setListener((type_fragment, data) -> listener_change.change_fragment_parameter(type_fragment,data));
        presenter = new TipsPresenter(this,getContext());
        adapter_pager = new ViewPagerAdapter(getChildFragmentManager());
        adapter_pager.addFragment(new TipsforMyPets(), getString(R.string.pager_discover));
        adapter_pager.addFragment(new Tips(), getString(R.string.pager_more_views));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        view_pager_tips.setOffscreenPageLimit(3);
        view_pager_tips.setAdapter(adapter_pager);
        view_pager_tips.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    change_tab_seguidos();
                }else{
                    change_tab_para_ti();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        view_pager_tips.setCurrentItem(1);
        change_tab_para_ti();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
        listener_change= (changue_fragment_parameters_listener) context;
        listener_wifi =(conexion_listener) context;
    }

    @Override
    public void showTips(ArrayList<TipsBean> tips_list,ArrayList<PostBean> helps) {}

    @Override
    public void showMoreTips(ArrayList<TipsBean> tips_list) { }

    @Override
    public void showTipsForMyPets(ArrayList<TipsBean> tips_list) {

    }

    @Override
    public void noInternet() {}

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();
        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }
    @Override
    public void peticionError() {
       presenter.getTips();
    }

    @Override
    public void have_pets(boolean have) {

    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void insertAdsInMenuItemsBelow(ArrayList<TipsBean> tips_list) {
        ArrayList<Object> data_below = new ArrayList<>();
        if(tips_list.size()>0) {
            data_below.addAll(tips_list);
            PAGINADOR = tips_list.get(tips_list.size() - 1).getId();
            mNativeAds.clear();
            mNativeAds.addAll(App.getInstance().getMoreAds());
            if(mNativeAds.size() > 0) {
                int offset = mNativeAds.size() + 1;
                int index = 3;
                for (UnifiedNativeAd ad: mNativeAds) {
                    if(index< data_below.size())
                        data_below.add(index, ad);
                    if((offset % 2 != 0)) {}
                    else{offset +=1;}
                    index = index + offset;
                }
                adapter.setMoreTips(data_below);

            }else{
                adapter.setMoreTips(data_below);

            }
        }

    }


    private void insertAdsInMenuItems(boolean more) {
        mNativeAds.clear();
        mNativeAds.addAll(App.getInstance().getMoreAds());
        if(mNativeAds.size() >  0) {
            int offset = mNativeAds.size() + 1;
            int index = 3;
            for (UnifiedNativeAd ad: mNativeAds) {
                if(index< data.size())
                    data.add(index, ad);
                if((offset % 2 != 0)) {}
                else{offset +=1;}
                index = index + offset;
            }
            adapter.setData(data);
        }else{
            adapter.setData(data);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void stop_player(){
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

    public void change_tab_para_ti(){
        tab_para_ti.setTextSize(16);
        tab_para_ti.setTextColor(getActivity().getResources().getColor(R.color.primary));
        tab_seguidos.setTextSize(12);
        tab_seguidos.setTextColor(Color.BLACK);
    }

    public void change_tab_seguidos(){
        tab_para_ti.setTextSize(12);
        tab_para_ti.setTextColor(Color.BLACK);
        tab_seguidos.setTextSize(16);
        tab_seguidos.setTextColor(getActivity().getResources().getColor(R.color.primary));
    }

}


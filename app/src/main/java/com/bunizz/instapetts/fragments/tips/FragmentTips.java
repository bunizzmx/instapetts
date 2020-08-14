package com.bunizz.instapetts.fragments.tips;

import android.content.Context;
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
import com.bunizz.instapetts.fragments.tips.adapters.TipsAdapter;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTips extends Fragment implements  TipsContract.View {
    @BindView(R.id.list_tips)
    ExoPlayerRecyclerViewTips list_tips;

    change_instance listener;
    changue_fragment_parameters_listener listener_change;
    TipsAdapter adapter;
    TipsPresenter presenter;
    ArrayList<Object> data = new ArrayList<>();
    conexion_listener listener_wifi;
    @BindView(R.id.root_loading)
    RelativeLayout root_loading;


    @BindView(R.id.spin_kit)
    SpinKitView spin_kit;

    @BindView(R.id.label_toolbar)
    TextView label_toolbar;

    @BindView(R.id.new_story)
    RelativeLayout new_story;

    @BindView(R.id.smoot_progress)
    SmoothProgressBar smoot_progress;


    @BindView(R.id.root_no_internet)
    RelativeLayout root_no_internet;

    @BindView(R.id.animated_title)
    TyperTextView animated_title;

    @BindView(R.id.open_notifications)
    RelativeLayout open_notifications;
    private boolean loading =true;
    private boolean IS_ALL = false;
    int PAGINADOR = -999;

    @BindView(R.id.refresh_tips)
    SwipeRefreshLayout refresh_tips;
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    public static FragmentTips newInstance() {
        return new FragmentTips();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TipsAdapter(getContext());
        adapter.setListener((type_fragment, data) -> listener_change.change_fragment_parameter(type_fragment,data));
        presenter = new TipsPresenter(this,getContext());
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
        list_tips.setLayoutManager(new LinearLayoutManager(getContext()));
        list_tips.setAdapter(adapter);
        adapter.setListener_video(new PlayStopVideoListener() {
            @Override
            public void StopVideo() {
                list_tips.onPausePlayer();
            }

            @Override
            public void MuteVideo() {
                list_tips.mute();
            }
        });
        adapter.setRequestManager(initGlide());
        presenter.getTips();
        refresh_tips.setOnRefreshListener(() ->{
            IS_ALL = false;
            smoot_progress.setVisibility(View.GONE);
            root_no_internet.setVisibility(View.GONE);
            presenter.getTips();
        });
        open_notifications.setVisibility(View.GONE);
        Style style = Style.values()[12];
        Sprite drawable = SpriteFactory.create(style);
        spin_kit.setIndeterminateDrawable(drawable);
        spin_kit.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        label_toolbar.setText(getActivity().getResources().getString(R.string.tips_notice));
        animated_title.animateText(getActivity().getResources().getString(R.string.tips_notice));
        animated_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animated_title.animateText(getActivity().getResources().getString(R.string.tips_notice));
            }
        });

        new_story.setVisibility(View.GONE);
        list_tips.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (dy > 0) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if(loading){
                            loading = false;
                            if(IS_ALL == false) {
                                Log.e("GENT_MORE_TIPS","SI");
                                smoot_progress.setVisibility(View.VISIBLE);
                                presenter.getMoreTips(PAGINADOR);
                            }else {
                                smoot_progress.setVisibility(View.GONE);
                                Log.e("GENT_MORE_TIPS","NO");
                            }
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
        listener_change= (changue_fragment_parameters_listener) context;
        listener_wifi =(conexion_listener) context;
    }

    @Override
    public void showTips(ArrayList<TipsBean> tips_list,ArrayList<PostBean> helps) {
        Log.e("GENT_MORE_TIPS","SHOW FIRST");
        loading = true;
        if(tips_list!=null) {
            data.clear();
            PAGINADOR= tips_list.get(tips_list.size()-1).getId();
            data.addAll(tips_list);
            interpolateHelps(helps);
        }
    }

    @Override
    public void showMoreTips(ArrayList<TipsBean> tips_list) {
        Log.e("GENT_MORE_TIPS","SHOW MORE");
        smoot_progress.setVisibility(View.GONE);
        if(tips_list.size()==0)
            IS_ALL = true;
        loading = true;
         insertAdsInMenuItemsBelow(tips_list);
    }

    @Override
    public void noInternet() {
        refresh_tips.setRefreshing(false);
        root_loading.setVisibility(View.GONE);
        root_no_internet.setVisibility(View.VISIBLE);
        listener_wifi.noWifiRequest();
    }

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
            if (mNativeAds.size() <= 0) { return;}
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
            refresh_tips.setRefreshing(false);
        }

    }


    private void insertAdsInMenuItems(boolean more) {
        mNativeAds.clear();
        mNativeAds.addAll(App.getInstance().getMoreAds());
        if (mNativeAds.size() <= 0) { return;}
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
        refresh_tips.setRefreshing(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(list_tips!=null)
            list_tips.onPausePlayer();
    }


    public void stop_player(){
        if(list_tips!=null)
            list_tips.onPausePlayer();
    }

    private void interpolateHelps(ArrayList<PostBean> helps) {
        if (helps.size() <= 0) { return;}
        int offset = helps.size() + 1;
        int index = 3;
        for (PostBean ad: helps) {
            if(index< data.size())
                data.add(index, ad);
            if((offset % 2 != 0)) {}
            else{offset +=1;}
            index = index + offset;
        }

        refresh_tips.setRefreshing(false);
        root_loading.setVisibility(View.GONE);
        list_tips.setMediaObjects(data);
        if(data.size()>3 && App.getInstance().getMoreAds().size()>0)
            insertAdsInMenuItems(true);
        else
            adapter.setData(data);
    }
}


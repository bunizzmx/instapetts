package com.bunizz.instapetts.fragments.tips;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.TipsBean;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.post.adapters.GaleryAdapter;
import com.bunizz.instapetts.fragments.tips.adapters.TipsAdapter;
import com.bunizz.instapetts.listeners.PlayStopVideoListener;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;
import com.bunizz.instapetts.utils.video_player.ExoPlayerRecyclerView;
import com.bunizz.instapetts.utils.video_player.ExoPlayerRecyclerViewTips;

import java.util.ArrayList;

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

    @BindView(R.id.root_loading)
    RelativeLayout root_loading;


    @BindView(R.id.spin_kit)
    SpinKitView spin_kit;

    @BindView(R.id.label_toolbar)
    TextView label_toolbar;

    @BindView(R.id.new_story)
    RelativeLayout new_story;


    @BindView(R.id.root_no_internet)
    RelativeLayout root_no_internet;

    @BindView(R.id.open_notifications)
    RelativeLayout open_notifications;


    @BindView(R.id.refresh_tips)
    SwipeRefreshLayout refresh_tips;

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
            root_no_internet.setVisibility(View.GONE);
            presenter.getTips();
        });
        open_notifications.setVisibility(View.GONE);
        Style style = Style.values()[14];
        Sprite drawable = SpriteFactory.create(style);
        spin_kit.setIndeterminateDrawable(drawable);
        spin_kit.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        label_toolbar.setText("Tips y Noticias");
        new_story.setVisibility(View.GONE);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
        listener_change= (changue_fragment_parameters_listener) context;
    }

    @Override
    public void showTips(ArrayList<TipsBean> tips_list) {
        if(tips_list!=null) {
            data.clear();
            data.addAll(tips_list);
            refresh_tips.setRefreshing(false);
            root_loading.setVisibility(View.GONE);
            list_tips.setMediaObjects(data);
            adapter.setData(data);
        }
    }

    @Override
    public void noInternet() {
        refresh_tips.setRefreshing(false);
        root_loading.setVisibility(View.GONE);
        root_no_internet.setVisibility(View.VISIBLE);
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
        Log.e("LIFECICLE","STOP");
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}


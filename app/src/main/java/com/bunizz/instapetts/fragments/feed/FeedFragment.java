package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.camera_history.CameraHistoryActivity;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.open_camera_histories_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.dilogs.DialogOptionsPosts;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedFragment extends Fragment implements  FeedContract.View{



    @BindView(R.id.feed_list)
    RecyclerView feed_list;

    changue_fragment_parameters_listener listener;
    FeedAdapter feedAdapter;

    @BindView(R.id.refresh_feed)
    SwipeRefreshLayout refresh_feed;
    open_camera_histories_listener listener_open_camera_h;

    ArrayList<Object> data = new ArrayList<>();

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }
    FeedPresenter mPresenter;


    @OnClick(R.id.open_notifications)
    void open_notifications()
    {
        listener.change_fragment_parameter(FragmentElement.INSTANCE_NOTIFICATIONS,null);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new FeedPresenter(this, getContext());
        HistoriesBean my_storie_bean = new HistoriesBean();
        if(mPresenter.getMyStories().size()>0) {
            my_storie_bean = mPresenter.getMyStories().get(0);
            data.add(my_storie_bean);
        }else {
            data.add(new HistoriesBean());
        }
        feedAdapter = new FeedAdapter(getContext(),data);
        feedAdapter.setListener(new changue_fragment_parameters_listener() {
            @Override
            public void change_fragment_parameter(int type_fragment, Bundle data) {
                listener.change_fragment_parameter(type_fragment,data);
            }
        });
        feedAdapter.setListener_open_h(() -> {
            if(listener_open_camera_h!=null){
                listener_open_camera_h.open();
            }

        });
        feedAdapter.setListener_post(new postsListener() {
            @Override
            public void onLike(int id_post) {
                PostActions postActions = new PostActions();
                postActions.setId_post(id_post);
                postActions.setAcccion("LIKE");
                postActions.setId_usuario("MYIDXXXX");
                postActions.setValor("1");
                mPresenter.likePost(postActions);
            }

            @Override
            public void onFavorite(int id_post,PostBean postBean) {
                PostActions postActions = new PostActions();
                postActions.setId_post(id_post);
                postActions.setAcccion("FAVORITE");
                postActions.setId_usuario("MYIDXXXX");
                postActions.setValor("1");
                mPresenter.saveFavorite(postActions,postBean);
            }

            @Override
            public void onDisfavorite(int id_post) {

            }

            @Override
            public void openMenuOptions() {
                DialogOptionsPosts optionsPosts = new DialogOptionsPosts(getContext());
                optionsPosts.show();
            }
        });

        mPresenter.get_feed();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        feed_list.setLayoutManager(new LinearLayoutManager(getContext()));
        feed_list.setAdapter(feedAdapter);
        refresh_feed.setOnRefreshListener(() ->{
           mPresenter.get_feed();
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
        listener_open_camera_h =(open_camera_histories_listener)context;
    }

    @Override
    public void show_feed(ArrayList<PostBean> data,ArrayList<HistoriesBean> data_stories) {
        refresh_feed.setRefreshing(false);
        ArrayList<HistoriesBean> historiesBeans = new ArrayList<>();
        ArrayList<Object> data_object= new ArrayList<>();
        if(mPresenter.getMyStories().size()>0){
            historiesBeans.add(mPresenter.getMyStories().get(0));
        }else{
            historiesBeans.add(new HistoriesBean());
        }
        if(data_stories!=null) {
            historiesBeans.addAll(data_stories);
            data_object.add(new HistoriesBean());
            data_object.addAll(data);
        }
        feedAdapter.setHistoriesBeans(historiesBeans);
        feedAdapter.addData(data_object);
    }

    @Override
    public void peticion_error() {
        mPresenter.get_feed();
    }
}


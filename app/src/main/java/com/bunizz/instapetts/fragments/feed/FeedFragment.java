package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
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
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedFragment extends Fragment implements  FeedContract.View{



    @BindView(R.id.feed_list)
    RecyclerView feed_list;

    change_instance listener;
    FeedAdapter feedAdapter;

    @BindView(R.id.refresh_feed)
    SwipeRefreshLayout refresh_feed;


    ArrayList<Object> data = new ArrayList<>();

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }
    FeedPresenter mPresenter;


    @OnClick(R.id.open_notifications)
    void open_notifications()
    {
        listener.change(FragmentElement.INSTANCE_NOTIFICATIONS);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data.add(new HistoriesBean());
        feedAdapter = new FeedAdapter(getContext(),data);
        feedAdapter.setListener((type_fragment, data) -> listener.change(type_fragment));
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
        });
        mPresenter = new FeedPresenter(this, getContext());
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
        listener= (change_instance) context;
    }

    @Override
    public void show_feed(ArrayList<PostBean> data,ArrayList<HistoriesBean> data_stories) {
        refresh_feed.setRefreshing(false);
        ArrayList<Object> data_object= new ArrayList<>();
        data_object.addAll(data);
        feedAdapter.setHistoriesBeans(data_stories);
        feedAdapter.addData(data_object);
    }
}


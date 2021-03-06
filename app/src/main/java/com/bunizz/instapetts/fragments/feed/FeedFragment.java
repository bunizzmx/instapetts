package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.camera_history.CameraHistoryActivity;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.listeners.actions_dialog_profile;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.open_camera_histories_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.dilogs.DialogOptionsPosts;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;
import com.bunizz.instapetts.utils.video_player.ExoPlayerRecyclerView;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedFragment extends Fragment implements  FeedContract.View{

    private boolean firstTime = true;

   /* @BindView(R.id.feed_list)
    RecyclerView feed_list;*/

    @BindView(R.id.exoPlayerRecyclerView)
    ExoPlayerRecyclerView mRecyclerView;


    @BindView(R.id.spin_kit)
    SpinKitView spin_kit;

    @BindView(R.id.root_no_internet)
    RelativeLayout root_no_internet;


    changue_fragment_parameters_listener listener;
    FeedAdapter feedAdapter;

    @BindView(R.id.refresh_feed)
    SwipeRefreshLayout refresh_feed;

    open_camera_histories_listener listener_open_camera_h;
     FollowsHelper followsHelper;
    ArrayList<Object> data = new ArrayList<>();
    boolean HAS_FRIENDS =false;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }
    FeedPresenter mPresenter;


    @OnClick(R.id.open_notifications)
    void open_notifications()
    {
        listener.change_fragment_parameter(FragmentElement.INSTANCE_NOTIFICATIONS,null);
    }

    @OnClick(R.id.new_story)
    void new_story()
    {
        if(listener_open_camera_h!=null){
            listener_open_camera_h.open();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new FeedPresenter(this, getContext());
        followsHelper = new FollowsHelper(getContext());
        if(followsHelper.getMyFriendsForPost().size()>0)
            HAS_FRIENDS =true;
        else
            HAS_FRIENDS=false;

        HistoriesBean my_storie_bean = new HistoriesBean();
        if(mPresenter.getMyStories().getHistories().size()>0) {
            my_storie_bean = mPresenter.getMyStories();
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
            public void onLike(int id_post,boolean type_like) {
                PostActions postActions = new PostActions();
                postActions.setId_post(id_post);
                if(type_like)
                postActions.setAcccion("1");
                else
                postActions.setAcccion("2");
                postActions.setId_usuario(App.read(PREFERENCES.UUID,"INVALID"));
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
            public void openMenuOptions(int id_post,int id_usuario,String uuid) {
                DialogOptionsPosts optionsPosts = new DialogOptionsPosts(getContext(),id_post,id_usuario,uuid);
                optionsPosts.setListener(id_post1 -> {
                    PostBean postBean = new PostBean();
                    postBean.setId_post_from_web(id_post1);
                    postBean.setTarget("DELETE");
                    mPresenter.deletePost(postBean);
                });
                optionsPosts.show();
            }
        });


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

        /*feed_list.setLayoutManager(new LinearLayoutManager(getContext()));
        feed_list.setAdapter(feedAdapter);*/
        feedAdapter.setRequestManager(initGlide());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(feedAdapter);

        refresh_feed.setOnRefreshListener(() ->{
            root_no_internet.setVisibility(View.GONE);
            if(followsHelper.getMyFriendsForPost().size()>0)
                HAS_FRIENDS =true;
            else
                HAS_FRIENDS=false;

            if(HAS_FRIENDS){
                mPresenter.get_feed(false, App.read(PREFERENCES.ID_USER_FROM_WEB,0));
            }else{
                mPresenter.geet_feed_recomended(false, App.read(PREFERENCES.ID_USER_FROM_WEB,0));
            }
        });
        Style style = Style.values()[6];
        Sprite drawable = SpriteFactory.create(style);
        spin_kit.setIndeterminateDrawable(drawable);
        spin_kit.setColor(getContext().getResources().getColor(R.color.primary));
        if(HAS_FRIENDS){
            mPresenter.get_feed(false, App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        }else{
            mPresenter.geet_feed_recomended(false, App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        }



    }
    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();
        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
        listener_open_camera_h =(open_camera_histories_listener)context;
    }

    @Override
    public void show_feed(ArrayList<PostBean> data,ArrayList<IndividualDataPetHistoryBean> data_stories) {
        ArrayList<IndividualDataPetHistoryBean> story_by_user=new ArrayList<>();
        refresh_feed.setRefreshing(false);
        ArrayList<HistoriesBean> historiesBeans = new ArrayList<>();
        ArrayList<Object> data_object= new ArrayList<>();
        if(mPresenter.getMyStories().getHistories().size()>0){
            historiesBeans.add(mPresenter.getMyStories());
        }else{
            historiesBeans.add(new HistoriesBean());
        }
        if(data_stories!=null) {
            Collections.sort(data_stories);
            for(int i =0;i<data_stories.size();i++){
               if(i>1){
                  if(data_stories.get(i).getId_user() == data_stories.get(i - 1).getId_user()){
                      story_by_user.add(data_stories.get(i));
                  }else{
                      historiesBeans.add(new HistoriesBean(
                              data_stories.get(i - 1).getName_user(),
                              data_stories.get(i - 1).getId_user(),
                              data_stories.get(i - 1).getPhoto_user(),
                              story_by_user));
                      story_by_user.clear();
                  }
               }else{
                   story_by_user.add(data_stories.get(0));
               }
            }
            spin_kit.setVisibility(View.GONE);
            data_object.add(new HistoriesBean());
            data_object.addAll(data);
            mRecyclerView.setMediaObjects(data_object);
        }
        feedAdapter.setHistoriesBeans(historiesBeans);
        feedAdapter.addData(data_object);
    }

    @Override
    public void show_feed_recomended(ArrayList<PostBean> data) {
        refresh_feed.setRefreshing(false);
        spin_kit.setVisibility(View.GONE);
        ArrayList<HistoriesBean> historiesBeans = new ArrayList<>();
        if(mPresenter.getMyStories().getHistories().size()>0){
            historiesBeans.add(mPresenter.getMyStories());
        }else{
            historiesBeans.add(new HistoriesBean());
        }

        ArrayList<Object> data_object= new ArrayList<>();
        data_object.add(new HistoriesBean());
        ArrayList<Object> data_recomended = new ArrayList<>();
        data_recomended.clear();
        data_recomended.addAll(data);
        feedAdapter.setHistoriesBeans(historiesBeans);
        feedAdapter.addData(data_object);
        feedAdapter.setData_recomended(data_recomended);
    }

    @Override
    public void peticion_error() {
        mPresenter.get_feed(false,App.read(PREFERENCES.ID_USER_FROM_WEB,0));
    }

    @Override
    public void deletePostError(boolean deleted) {
        if(deleted)
            Toast.makeText(getContext(),"POST ELIMINADO",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getContext(),"NO SE PUDO BORRAR EL POSTS INTENTA DE NUEVO",Toast.LENGTH_LONG).show();
    }

    @Override
    public void LikeEror() {

    }

    @Override
    public void LikeSuccess() {

    }

    @Override
    public void noInternet() {
        refresh_feed.setRefreshing(false);
        spin_kit.setVisibility(View.GONE);
        root_no_internet.setVisibility(View.VISIBLE);
    }


    @Override
    public void onPause() {
        super.onPause();
        if(mRecyclerView!=null)
            mRecyclerView.onPausePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mRecyclerView!=null){}
           // mRecyclerView.releasePlayer();
    }
}


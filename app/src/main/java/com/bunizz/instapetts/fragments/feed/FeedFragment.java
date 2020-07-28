package com.bunizz.instapetts.fragments.feed;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.reports.ReportActiviy;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.actions_dialog_profile;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.conexion_listener;
import com.bunizz.instapetts.listeners.open_camera_histories_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.ProgressCircle;
import com.bunizz.instapetts.utils.dilogs.DialogOptionsPosts;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;
import com.bunizz.instapetts.utils.video_player.ExoPlayerRecyclerView;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedFragment extends Fragment implements  FeedContract.View{

    private boolean firstTime = true;

   /* @BindView(R.id.feed_list)
    RecyclerView feed_list;*/

    @BindView(R.id.exoPlayerRecyclerView)
    ExoPlayerRecyclerView mRecyclerView;

    @BindView(R.id.cirlce_progress)
    ProgressCircle cirlce_progress;

    @BindView(R.id.root_no_internet)
    RelativeLayout root_no_internet;

    @BindView(R.id.badge_notification)
    CardView badge_notification;

    changue_fragment_parameters_listener listener;
    FeedAdapter feedAdapter;

    @BindView(R.id.refresh_feed)
    SwipeRefreshLayout refresh_feed;

    open_camera_histories_listener listener_open_camera_h;
     conexion_listener listener_wifi;
     IdsUsersHelper followsHelper;
    ArrayList<Object> data_feed = new ArrayList<>();
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    boolean HAS_FRIENDS =false;
    final ValueAnimator valueAnimator = ValueAnimator.ofInt(1,360);
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
        followsHelper = new IdsUsersHelper(getContext());
        if(followsHelper.getMyFriendsForPost().size()>0)
            HAS_FRIENDS =true;
        else
            HAS_FRIENDS=false;

        feedAdapter = new FeedAdapter(getContext(), data_feed);
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
            public void onLike(int id_post,boolean type_like,int id_usuario,String url_image) {
                PostActions postActions = new PostActions();
                postActions.setId_post(id_post);
                if(type_like)
                postActions.setAcccion("1");
                else
                postActions.setAcccion("2");
                postActions.setId_usuario(id_usuario);
                postActions.setValor("1");
                postActions.setExtra(url_image);
                if(id_usuario != App.read(PREFERENCES.ID_USER_FROM_WEB,0))
                mPresenter.likePost(postActions);
            }

            @Override
            public void onFavorite(int id_post,PostBean postBean) {
                PostActions postActions = new PostActions();
                postActions.setId_post(id_post);
                postActions.setAcccion("FAVORITE");
                postActions.setId_usuario(postBean.getId_usuario());
                postActions.setValor("1");
                mPresenter.saveFavorite(postActions,postBean);
            }

            @Override
            public void onDisfavorite(int id_post) {

            }

            @Override
            public void openMenuOptions(int id_post,int id_usuario,String uuid) {
                DialogOptionsPosts optionsPosts = new DialogOptionsPosts(getContext(),id_post,id_usuario,uuid);
                optionsPosts.setListener(new actions_dialog_profile() {
                    @Override
                    public void delete_post(int id_post) {
                        PostBean postBean = new PostBean();
                        postBean.setId_post_from_web(id_post);
                        postBean.setId_usuario(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
                        postBean.setTarget(WEBCONSTANTS.DELETE);
                        mPresenter.deletePost(postBean);
                    }

                    @Override
                    public void reportPost(int id_post) {
                        Intent reportIntent = new Intent(getActivity(),ReportActiviy.class);
                        reportIntent.putExtra("ID_RECURSO",id_post);
                        reportIntent.putExtra("TYPO_RECURSO",1);
                        startActivity(reportIntent);
                    }

                    @Override
                    public void unfollowUser(int id_user,String uuid) {
                        mPresenter.unfollowUser(uuid,id_user);
                    }
                });
                optionsPosts.show();
            }

            @Override
            public void commentPost(int id_post,boolean can_comment,int id_usuario) {
                Bundle b = new Bundle();
                b.putInt(BUNDLES.ID_POST,id_post);
                b.putBoolean(BUNDLES.CAN_COMMENT,can_comment);
                b.putInt(BUNDLES.ID_USUARIO,id_usuario);
                listener.change_fragment_parameter(FragmentElement.INSTANCE_COMENTARIOS,b);
            }

            @Override
            public void reproduceVideoActivity(PostBean postBean) {

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
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(animation -> {
            cirlce_progress.setValue((Integer) animation.getAnimatedValue());
        });
        valueAnimator.start();
        feedAdapter.setRequestManager(initGlide());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
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
        Style style = Style.values()[12];
        Sprite drawable = SpriteFactory.create(style);
        if(HAS_FRIENDS)
            mPresenter.get_feed(false, App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        else
            mPresenter.geet_feed_recomended(false, App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        mPresenter.haveNotificatiosn();
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
        listener_wifi =(conexion_listener) context;
    }

    @Override
    public void show_feed(ArrayList<PostBean> data,ArrayList<HistoriesBean> data_stories) {
        Log.e("SHOW_FEED","SI");
        refresh_feed.setRefreshing(false);
        if(data.size()>0)
            data_feed.clear();

        ArrayList<HistoriesBean> historiesBeans = new ArrayList<>();
        if(!mPresenter.getMyStories().getHistorias().isEmpty()){
            historiesBeans.add(mPresenter.getMyStories());
        }else{
            historiesBeans.add(new HistoriesBean());
        }
        historiesBeans.addAll(data_stories);
        cirlce_progress.setVisibility(View.GONE);
        valueAnimator.cancel();
        data_feed.add(new HistoriesBean());
        data_feed.addAll(data);
        mRecyclerView.setMediaObjects(data_feed);
        feedAdapter.setHistoriesBeans(historiesBeans);
        if(App.getInstance().getMoreAds().size()>0)
            insertAdsInMenuItems(true);
        else
            feedAdapter.addData(data_feed);

    }

    @Override
    public void show_next_feed(ArrayList<PostBean> data) {

    }

    @Override
    public void show_feed_recomended(ArrayList<PostBean> data, ArrayList<UserBean> users) {
        refresh_feed.setRefreshing(false);
        cirlce_progress.setVisibility(View.GONE);
        valueAnimator.cancel();
        ArrayList<HistoriesBean> historiesBeans = new ArrayList<>();
        if(!mPresenter.getMyStories().getHistorias().isEmpty()){
            historiesBeans.add(mPresenter.getMyStories());
        }else{
            historiesBeans.add(new HistoriesBean());
        }
        ArrayList<Object> users_object = new ArrayList<>();
        ArrayList<Object> data_object= new ArrayList<>();
        data_object.add(new HistoriesBean());
        ArrayList<Object> data_recomended = new ArrayList<>();
        data_recomended.clear();
        data_recomended.addAll(data);
        users_object.addAll(users);
        feedAdapter.setHistoriesBeans(historiesBeans);
        feedAdapter.addData(data_object);
        Log.e("DATATATATA","-->"+ users_object.size());
        feedAdapter.setData_recomended(data_recomended,users_object);

    }

    @Override
    public void peticion_error() {
        mPresenter.get_feed(false,App.read(PREFERENCES.ID_USER_FROM_WEB,0));
    }

    @Override
    public void deletePostError(boolean deleted) {
        if(deleted) {
            Log.e("DELETED_POST","TRUE");
            getActivity().onBackPressed();
            Toast.makeText(getContext(), "POST ELIMINADO", Toast.LENGTH_LONG).show();
        }
        else {
            Log.e("DELETED_POST","FALSE");
            Toast.makeText(getContext(), "NO SE PUDO BORRAR EL POSTS INTENTA DE NUEVO", Toast.LENGTH_LONG).show();
        }
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
        cirlce_progress.setVisibility(View.GONE);
        valueAnimator.cancel();
        root_no_internet.setVisibility(View.VISIBLE);
        listener_wifi.noWifiRequest();
    }

    @Override
    public void showBadge(boolean show) {
        if(show)
            badge_notification.setVisibility(View.VISIBLE);
        else
            badge_notification.setVisibility(View.GONE);
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


    public void stop_player(){
        if(mRecyclerView!=null)
            mRecyclerView.onPausePlayer();
    }


    private void insertAdsInMenuItems(boolean more) {
        mNativeAds.clear();
        mNativeAds.addAll(App.getInstance().getMoreAds());
        if (mNativeAds.size() <= 0) { return;}
        int offset = mNativeAds.size() + 1;
        int index = 3;
        for (UnifiedNativeAd ad: mNativeAds) {
            if(index< data_feed.size())
                data_feed.add(index, ad);
            if((offset % 2 != 0)) {}
            else{offset +=1;}
            index = index + offset;
        }
        feedAdapter.addData(data_feed);
        refresh_feed.setRefreshing(false);
    }


}


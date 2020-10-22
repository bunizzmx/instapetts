package com.bunizz.instapetts.fragments.feed;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import com.bunizz.instapetts.activitys.reports.ReportActiviy;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.camera.CameraStoryt;
import com.bunizz.instapetts.fragments.feed.adapters.FeedAdapter;
import com.bunizz.instapetts.fragments.feed.fragments.Feed;
import com.bunizz.instapetts.fragments.feed.fragments.FeedParaTi;
import com.bunizz.instapetts.fragments.search.posts.FragmentListGalery;
import com.bunizz.instapetts.listeners.actions_dialog_profile;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.conexion_listener;
import com.bunizz.instapetts.listeners.open_camera_histories_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.ProgressCircle;
import com.bunizz.instapetts.utils.dilogs.DialogOptionsPosts;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;
import com.bunizz.instapetts.utils.smoot.SmoothProgressBar;
import com.bunizz.instapetts.utils.video_player.ExoPlayerRecyclerView;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedViewPager extends Fragment implements  FeedContract.View{

    private boolean firstTime = true;

   /* @BindView(R.id.feed_list)
    RecyclerView feed_list;*/


    @BindView(R.id.badge_notification)
    CardView badge_notification;

    @BindView(R.id.num_badge)
    TextView num_badge;

    changue_fragment_parameters_listener listener;
    FeedAdapter feedAdapter;

    @BindView(R.id.view_pager_feed)
    ViewPager view_pager_feed;

    @BindView(R.id.tab_seguidos)
    TextView tab_seguidos;

    @BindView(R.id.tab_para_ti)
    TextView tab_para_ti;

    boolean IS_FEED_RECOMENDED = false;


    ViewPagerAdapter adapter_pager;


    open_camera_histories_listener listener_open_camera_h;
     conexion_listener listener_wifi;
     IdsUsersHelper followsHelper;
    ArrayList<Object> data_feed = new ArrayList<>();
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    boolean HAS_FRIENDS =false;
    final ValueAnimator valueAnimator = ValueAnimator.ofInt(1,360);
    public static FeedViewPager newInstance() {
        return new FeedViewPager();
    }
    FeedPresenter mPresenter;
    private boolean loading =true;
    private boolean IS_ALL = false;
    int PAGINADOR = -999;

    @OnClick(R.id.open_notifications)
    void open_notifications()
    {
        listener.change_fragment_parameter(FragmentElement.INSTANCE_NOTIFICATIONS,null);
    }

    @OnClick(R.id.new_story)
    void new_story()
    {
        if(!App.read(PREFERENCES.MODO_INVITADO,false)) {
            if (listener_open_camera_h != null) {
                listener_open_camera_h.open();
            }
        }else
        {
            listener_wifi.message(getActivity().getString(R.string.no_action_invitado));
        }
    }

    @OnClick(R.id.tab_para_ti)
    void tab_para_ti()
    {
        view_pager_feed.setCurrentItem(1);
       change_tab_para_ti();
    }
    @OnClick(R.id.tab_seguidos)
    void tab_seguidos()
    {
        view_pager_feed.setCurrentItem(0);
        change_tab_seguidos();
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

        feedAdapter = new FeedAdapter(getActivity(), data_feed);
        feedAdapter.setListener(new changue_fragment_parameters_listener() {
            @Override
            public void change_fragment_parameter(int type_fragment, Bundle data) {
                listener.change_fragment_parameter(type_fragment,data);
            }
        });
        feedAdapter.setListener_open_h(() -> {
            if(!App.read(PREFERENCES.MODO_INVITADO,false)) {
                if (listener_open_camera_h != null)
                    listener_open_camera_h.open();
            }else
                listener_wifi.message(getActivity().getString(R.string.no_action_invitado));
        });
        feedAdapter.setListener_post(new postsListener() {
            @Override
            public void onLike(int id_post,boolean type_like,int id_usuario,String url_image) {
                if(!App.read(PREFERENCES.MODO_INVITADO,false)) {
                    PostActions postActions = new PostActions();
                    postActions.setId_post(id_post);
                    if (type_like)
                        postActions.setAcccion("1");
                    else
                        postActions.setAcccion("2");
                    postActions.setId_usuario(id_usuario);
                    postActions.setValor("1");
                    postActions.setExtra(url_image);
                    if (id_usuario != App.read(PREFERENCES.ID_USER_FROM_WEB, 0))
                        mPresenter.likePost(postActions);
                }else{
                    listener_wifi.message(getContext().getString(R.string.no_action_invitado));
                }
            }

            @Override
            public void onFavorite(int id_post,PostBean postBean) {
                if(!App.read(PREFERENCES.MODO_INVITADO,false)) {
                    PostActions postActions = new PostActions();
                    postActions.setId_post(id_post);
                    postActions.setAcccion("FAVORITE");
                    postActions.setId_usuario(postBean.getId_usuario());
                    postActions.setValor("1");
                    mPresenter.saveFavorite(postActions, postBean);
                }
                else{
                    listener_wifi.message(getContext().getString(R.string.no_action_invitado));
                }
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
                b.putInt(BUNDLES.TYPE_RESOURCE_TO_COMMNET,1);
                listener.change_fragment_parameter(FragmentElement.INSTANCE_COMENTARIOS,b);
            }

            @Override
            public void reproduceVideoActivity(PostBean postBean) {

            }
        });
        adapter_pager = new ViewPagerAdapter(getChildFragmentManager());
        adapter_pager.addFragment(new Feed(), getString(R.string.pager_discover));
        adapter_pager.addFragment(new FeedParaTi(), getString(R.string.pager_more_views));

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
        view_pager_feed.setOffscreenPageLimit(3);
        view_pager_feed.setAdapter(adapter_pager);
        view_pager_feed.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
     view_pager_feed.setCurrentItem(1);
     change_tab_para_ti();
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

    }

    @Override
    public void show_next_feed(ArrayList<PostBean> data) {

    }

    @Override
    public void show_feed_recomended(ArrayList<PostBean> data, ArrayList<UserBean> users) {


    }

    @Override
    public void peticion_error() {
        mPresenter.get_feed(false,App.read(PREFERENCES.ID_USER_FROM_WEB,0));
    }

    @Override
    public void deletePostError(boolean deleted) {
        if(deleted) {
            getActivity().onBackPressed();
            Toast.makeText(getContext(), getContext().getString(R.string.delete_post_succes), Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(), getContext().getString(R.string.delete_post_error), Toast.LENGTH_LONG).show();
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

    }

    @Override
    public void showBadge(boolean show,int num) {
        if(show)
            badge_notification.setVisibility(View.VISIBLE);
        else
            badge_notification.setVisibility(View.GONE);

        num_badge.setText(""+num);
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void stop_player(){
        try {
            ((FeedParaTi) adapter_pager.getItem(1)).stop_player();
        }catch (Exception e){}
    }

    public void release_player(){
        try {
            ((FeedParaTi) adapter_pager.getItem(1)).release_player();
        }catch (Exception e){}
    }



    private void insertAdsInMenuItems(ArrayList<Object>  data,boolean more) {
        ArrayList<Object> current_data = new ArrayList<>();
        current_data.addAll(data);
        mNativeAds.clear();
        mNativeAds.addAll(App.getInstance().getMoreAds());
        if (mNativeAds.size() <= 0) { return;}
        int offset = mNativeAds.size() + 1;
        int index = 3;
        for (UnifiedNativeAd ad: mNativeAds) {
            if(index< current_data.size())
                current_data.add(index, ad);
            if((offset % 2 != 0)) {}
            else{offset +=1;}
            index = index + offset;
        }
        if(more)
            feedAdapter.addMoreFeed(current_data);
        else
           feedAdapter.addData(current_data);

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


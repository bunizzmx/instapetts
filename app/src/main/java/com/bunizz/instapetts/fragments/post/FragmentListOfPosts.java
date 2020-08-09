package com.bunizz.instapetts.fragments.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.PlayVideo.PlayVideoActivity;
import com.bunizz.instapetts.activitys.reports.ReportActiviy;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.fragments.feed.FeedPresenter;
import com.bunizz.instapetts.fragments.post.adapters.GaleryAdapter;
import com.bunizz.instapetts.fragments.post.adapters.PostsAdapter;
import com.bunizz.instapetts.fragments.search.posts.more_post;
import com.bunizz.instapetts.listeners.actions_dialog_profile;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.dilogs.DialogOptionsPosts;
import com.bunizz.instapetts.utils.video_player.ExoPlayerRecyclerView;
import com.bunizz.instapetts.web.parameters.PostActions;

import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentListOfPosts extends Fragment implements FeedContract.View {
    @BindView(R.id.list_posts_publics_advanced)
    ExoPlayerRecyclerView list_posts_publics_advanced;

    changue_fragment_parameters_listener listener;
    PostsAdapter feedAdapter;
    FeedListPresenter mPresenter;

    ArrayList<Object> data = new ArrayList<>();

    public static FragmentListOfPosts newInstance() {
        return new FragmentListOfPosts();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new FeedListPresenter(this,getContext());
        feedAdapter = new PostsAdapter(getContext());
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
                        Intent reportIntent = new Intent(getActivity(), ReportActiviy.class);
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
                b.putInt(BUNDLES.TYPE_PET,0);
                b.putInt(BUNDLES.ID_USUARIO,id_usuario);
                listener.change_fragment_parameter(FragmentElement.INSTANCE_COMENTARIOS,b);
            }

            @Override
            public void reproduceVideoActivity(PostBean postBean) {
                Intent i = new Intent(getContext(), PlayVideoActivity.class);
                i.putExtra("TYPE_PLAYER", 1);
                i.putExtra(BUNDLES.POST_NAME,postBean.getName_user());
                i.putExtra(BUNDLES.POST_ASPECT,postBean.getAspect());
                i.putExtra(BUNDLES.POST_DESCRIPCION,postBean.getDescription());
                i.putExtra(BUNDLES.POST_FOTO_USER,postBean.getUrl_photo_user());
                i.putExtra(BUNDLES.POST_ID_POST,postBean.getId_post_from_web());
                i.putExtra(BUNDLES.POST_ID_USUARIO,postBean.getId_usuario());
                i.putExtra(BUNDLES.POST_IS_CENSORED,postBean.getCensored());
                i.putExtra(BUNDLES.POST_IS_LIKED,postBean.isLiked());
                i.putExtra(BUNDLES.POST_IS_SAVED,postBean.isSaved());
                i.putExtra(BUNDLES.POST_LIKES,postBean.getLikes());
                i.putExtra(BUNDLES.POST_THUMBH_VIDEO,postBean.getThumb_video());
                i.putExtra(BUNDLES.POST_TYPE,postBean.getType_post());
                i.putExtra(BUNDLES.POST_ASPECT,postBean.getAspect());
                Log.e("CLICK_PARSED","-->no esxxx post" +postBean.getUrls_posts());
                i.putExtra(BUNDLES.POST_URLS,postBean.getUrls_posts());
                i.putExtra(BUNDLES.POST_UUID,postBean.getUuid());
                //i.putExtra("BEAN", Parcels.wrap(postBean));
                if (postBean instanceof PostBean) {
                    Log.e("CLICK_PARSED", "-->2xxxxx");
                } else {
                    Log.e("CLICK_PARSED", "-->2yyyyy");
                }
                Log.e("CLICK_PARSED", "-->2");
                getContext().startActivity(i);

            }
        });

        feedAdapter.setRequestManager(initGlide());
        Bundle bundle=getArguments();
        if(bundle!=null){
            data = Parcels.unwrap(bundle.getParcelable("POSTS"));
            Log.e("FROM_PROFILE","--->size  : " + data.size());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_publics_advanced, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_posts_publics_advanced.setLayoutManager(new LinearLayoutManager(getContext()));
        list_posts_publics_advanced.setAdapter(feedAdapter);
        list_posts_publics_advanced.setMediaObjects(data);
        feedAdapter.addData(data);
        feedAdapter.setListener(new changue_fragment_parameters_listener() {
            @Override
            public void change_fragment_parameter(int type_fragment, Bundle data) {
                listener.change_fragment_parameter(type_fragment,data);
            }
        });
    }

    public void update_lists(){
        Bundle bundle=getArguments();
        if(bundle!=null){
            data = Parcels.unwrap(bundle.getParcelable("POSTS"));
            Log.e("FROM_PROFILE","--->size  : " + data.size());
            if(feedAdapter!=null) {
                list_posts_publics_advanced.setMediaObjects(data);
                feedAdapter.addData(data);
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();
        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(list_posts_publics_advanced!=null)
            list_posts_publics_advanced.onPausePlayer();
    }

    public void stop_player(){
        if(list_posts_publics_advanced!=null)
            list_posts_publics_advanced.onPausePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(list_posts_publics_advanced!=null){}
        // mRecyclerView.releasePlayer();
    }

    @Override
    public void show_feed(ArrayList<PostBean> data, ArrayList<HistoriesBean> data_stories) { }

    @Override
    public void show_next_feed(ArrayList<PostBean> data) {

    }

    @Override
    public void show_feed_recomended(ArrayList<PostBean> data,ArrayList<UserBean> users) { }

    @Override
    public void peticion_error() { }

    @Override
    public void deletePostError(boolean deleted) {
        if(deleted) {
            Log.e("DELETED_POST","TRUE");
            getActivity().onBackPressed();
            Toast.makeText(getContext(), getContext().getString(R.string.delete_post_succes), Toast.LENGTH_LONG).show();
        }
        else {
            Log.e("DELETED_POST","FALSE");
            Toast.makeText(getContext(), getContext().getString(R.string.delete_post_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void LikeEror() { }

    @Override
    public void LikeSuccess() { }

    @Override
    public void noInternet() {

    }

    @Override
    public void showBadge(boolean show) {
        
    }


}


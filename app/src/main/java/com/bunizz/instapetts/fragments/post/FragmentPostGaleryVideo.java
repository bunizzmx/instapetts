package com.bunizz.instapetts.fragments.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.PlayVideo.PlayVideoActivity;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.post.adapters.AdapterGridPostsProfileVideo;
import com.bunizz.instapetts.fragments.profile.adapters.AdapterGridPostsProfile;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.conexion_listener;
import com.bunizz.instapetts.listeners.get_current_pager_listener;
import com.bunizz.instapetts.listeners.postsListener;
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

public class FragmentPostGaleryVideo extends Fragment implements PostGaleryContract.View {
    @BindView(R.id.list_galery)
    RecyclerView list_galery;

    static final int PLAY_VIDEO_RESULT= 6;
    @BindView(R.id.root_no_data)
    RelativeLayout root_no_data;

    @BindView(R.id.title_no_data)
    TextView title_no_data;

    @BindView(R.id.body_no_data)
    TextView body_no_data;
    PostGaleryPresenter presenter;
    conexion_listener listener_wifi;
    changue_fragment_parameters_listener listener;
    AdapterGridPostsProfileVideo feedAdapter;
    ArrayList<Object> data_posts = new ArrayList<>();
    private boolean loading =true;
    private boolean IS_ALL = false;

    int PAGINADOR = -999;
    int ID_USER = -999;
    get_current_pager_listener listener_pager;

    public get_current_pager_listener getListener_pager() {
        return listener_pager;
    }

    public void setListener_pager(get_current_pager_listener listener_pager) {
        this.listener_pager = listener_pager;
    }

    public void setData_posts(ArrayList<Object> data_posts) {
        IS_ALL = false;
        PAGINADOR = 0;
        loading = true;
        if(data_posts!=null) {
            if (data_posts.size() > 0) {
                PostBean ID_POST = (PostBean) data_posts.get(data_posts.size() - 1);
                PAGINADOR = ID_POST.getId_post_from_web();
            } else {
                IS_ALL = true;
            }
            if (data_posts != null) {
                if (data_posts.size() > 0) {
                    this.data_posts.clear();
                    this.data_posts.addAll(data_posts);
                    if (feedAdapter != null)
                        feedAdapter.setPosts(this.data_posts);
                    if (root_no_data != null)
                        root_no_data.setVisibility(View.GONE);
                } else {
                    this.data_posts.clear();
                    feedAdapter.clear();
                    feedAdapter.refresh();
                    if (root_no_data != null)
                        root_no_data.setVisibility(View.VISIBLE);
                }
            } else {
                if (root_no_data != null)
                    root_no_data.setVisibility(View.VISIBLE);
            }
        }else{
            if (root_no_data != null)
                root_no_data.setVisibility(View.VISIBLE);
        }

    }

    public void setIdUser(int id){
        ID_USER = id;
    }




    public boolean isDataAdded(){
        if(this.data_posts.size()>0)
            return  true;
        else
            return  false;
    }
    public static FragmentPostGaleryVideo newInstance() {
        return new FragmentPostGaleryVideo();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedAdapter = new AdapterGridPostsProfileVideo(getContext());
        presenter = new PostGaleryPresenter(this,getContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_galery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_galery.setLayoutManager(new GridLayoutManager(getContext(),2));
        list_galery.setHasFixedSize(true);
        list_galery.setItemViewCacheSize(20);
        list_galery.setAdapter(feedAdapter);
        feedAdapter.setListener((type_fragment, data) -> {
            ArrayList<Object> object_currents = new ArrayList<>();
            int position = data.getInt("POSITION");
            Bundle b = new Bundle();
            for (int i = position;i<data_posts.size();i++){
                object_currents.add(data_posts.get(i));
            }
            b.putParcelable("POSTS", Parcels.wrap(object_currents));
            listener.change_fragment_parameter(type_fragment,b);
        });
        feedAdapter.setListener_post(new postsListener() {
            @Override
            public void onLike(int id_post, boolean type_like, int id_usuario, String url_image) {
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
                        presenter.likePost(postActions);
                }else{
                    listener_wifi.message(getContext().getString(R.string.no_action_invitado));
                }
            }

            @Override
            public void onFavorite(int id_post, PostBean postBean) {
                if(!App.read(PREFERENCES.MODO_INVITADO,false)) {
                    PostActions postActions = new PostActions();
                    postActions.setId_post(id_post);
                    postActions.setAcccion("FAVORITE");
                    postActions.setId_usuario(postBean.getId_usuario());
                    postActions.setValor("1");
                    presenter.saveFavorite(postActions, postBean);
                }
                else{
                    listener_wifi.message(getContext().getString(R.string.no_action_invitado));
                }
            }

            @Override
            public void onDisfavorite(int id_post) {

            }

            @Override
            public void openMenuOptions(int id_post, int id_usuario, String uuid) {

            }

            @Override
            public void commentPost(int id_post, boolean can_comment,int id_usuario) {

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
                getActivity().startActivityForResult(i,PLAY_VIDEO_RESULT);
            }
        });
        title_no_data.setText(getContext().getString(R.string.no_post_title));
        body_no_data.setText(getContext().getString(R.string.no_post_body));
        list_galery.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (dy > 0) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if(loading){
                            loading = false;
                            if(IS_ALL == false) {
                                if(listener_pager!=null)
                                   presenter.getMorePost(3,PAGINADOR,ID_USER,listener_pager.getCurrentItemPager());
                                else
                                    presenter.getMorePost(3,PAGINADOR,ID_USER,0);
                            }else {
                                Log.e("DONWLOAD_MORE_COMMENTS","NO");
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
        listener= (changue_fragment_parameters_listener) context;
         listener_wifi =(conexion_listener) context;

    }

    @Override
    public void showMorePost(ArrayList<PostBean> posts) {
        ArrayList<Object> data = new ArrayList<>();
        data.addAll(posts);
        if(posts.size()>0) {
            PAGINADOR = posts.get(posts.size() - 1).getId_post_from_web();
            if(posts.size() < 10 )
                IS_ALL = true;
        }else{
            IS_ALL = true;
        }
        Log.e("PAGINADOR_NETX","-->: " + PAGINADOR);
        loading = true;
        if(data_posts !=null){
            if(data_posts.size()>0){
                data_posts.addAll(posts);
                if(feedAdapter!=null)
                    feedAdapter.setPostsPaginate(data);
                if(root_no_data!=null)
                    root_no_data.setVisibility(View.GONE);
            }
        }
    }
}


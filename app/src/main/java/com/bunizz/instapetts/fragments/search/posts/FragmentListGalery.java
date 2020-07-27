package com.bunizz.instapetts.fragments.search.posts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.PlayVideo.PlayVideoActivity;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.search.AdapterGridPosts;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentListGalery extends Fragment implements  ListGaleryContract.View{
    @BindView(R.id.list_posts_publics)
    RecyclerView list_posts_publics;

    @BindView(R.id.root_no_internet)
    RelativeLayout root_no_internet;

    @BindView(R.id.title_no_internet)
    TextView title_no_internet;

    @BindView(R.id.body_no_data)
    TextView body_no_data;

    @BindView(R.id.icon_no_internet)
    ImageView icon_no_internet;


    @BindView(R.id.spin_kit)
    SpinKitView spin_kit;

    Style style = Style.values()[12];
    Sprite drawable = SpriteFactory.create(style);

    private boolean loading =true;
    private boolean IS_ALL = false;


    ListGaleryPresenter presenter;
    int TYPO_FRAGMENT =0;


    changue_fragment_parameters_listener listener;
    ArrayList<Object> data_posts = new ArrayList<>();
    GridLayoutManager layoutManager;
    AdapterGridPosts adapter;
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    public void setData_posts(ArrayList<Object> data_posts,int TYPO_FRAGMENT) {
        Log.e("POSTSTSTSD","--->" + data_posts.size());
        this.TYPO_FRAGMENT = TYPO_FRAGMENT;
        if(data_posts !=null){
            spin_kit.setVisibility(View.GONE);
            if(data_posts.size()>0){
                root_no_internet.setVisibility(View.GONE);
                this.data_posts.clear();
                this.data_posts.addAll(data_posts);
                Log.e("SETIE_DATA_POST","--> " + data_posts.size());
                if(adapter!=null) {
                        adapter.setPosts(this.data_posts);
                }
            }else{
                icon_no_internet.setVisibility(View.GONE);
                title_no_internet.setText("No hay Publicaciones");
                body_no_data.setText("Aun no hay publicaciones con esta opcion,quiza mas tarde.");
                root_no_internet.setVisibility(View.VISIBLE);
            }
        }else{
            icon_no_internet.setVisibility(View.GONE);
            title_no_internet.setText("No hay Publicaciones");
            body_no_data.setText("Aun no hay publicaciones con esta opcion,quiza mas tarde.");
            root_no_internet.setVisibility(View.VISIBLE);
        }

    }


    public boolean is_data_charged(){
        if(this.data_posts.size()>0)
            return  true;
        else
            return  false;
    }
    public static FragmentListGalery newInstance() {
        return new FragmentListGalery();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AdapterGridPosts(getContext());
        adapter.setListener_post(new postsListener() {
            @Override
            public void onLike(int id_post, boolean type_like, int id_usuario, String url_image) {
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
                presenter.likePost(postActions);
            }

            @Override
            public void onFavorite(int id_post, PostBean postBean) {
                PostActions postActions = new PostActions();
                postActions.setId_post(id_post);
                postActions.setAcccion("FAVORITE");
                postActions.setId_usuario(postBean.getId_usuario());
                postActions.setValor("1");
                presenter.saveFavorite(postActions,postBean);
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
                getContext().startActivity(i);
            }
        });
        presenter = new ListGaleryPresenter(this,getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_galery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        spin_kit.setIndeterminateDrawable(drawable);
        spin_kit.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        layoutManager = new GridLayoutManager(getContext(),3);
        list_posts_publics.setLayoutManager(layoutManager);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)) {
                    case 1:
                        return 1;
                    default:
                        return 3;
                }

            }
        });
        list_posts_publics.setAdapter(adapter);
        adapter.setListener((type_fragment, data) -> {
            int position = data.getInt("POSITION");
            Log.e("POSITION_SIZE","-->" + position + "/" + data_posts.size());
            Bundle b = new Bundle();
            for(int i =0;i< data_posts.size();i++) {
                if (data_posts.get(i) instanceof PostBean)
                    Log.e("CLICK_PARSED", ".---");
                else
                    Log.e("CLICK_PARSED", "NO ES UN POST");
            }
            b.putParcelable("POSTS", Parcels.wrap(data_posts));
            listener.change_fragment_parameter(type_fragment,b);
        });
        list_posts_publics.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                Log.e("DONWLOAD_MORE_COMMENTS","SI");
                                presenter.getMorePost(TYPO_FRAGMENT,adapter.get_ultimo_id());
                            }else {
                                Log.e("DONWLOAD_MORE_COMMENTS","NO");
                            }
                        }else{
                            Log.e("DONWLOAD_MORE_COMMENTS","ESTA CARGANDO");
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

    }





    private void insertAdsInMenuItems(boolean more) {
        loading = true;
        mNativeAds.clear();
        mNativeAds.addAll(App.getInstance().getMoreAds());
        if (mNativeAds.size() <= 0) { return;}
        int offset =9;
        int index = 9;
        for (UnifiedNativeAd ad: mNativeAds) {
            if(index< data_posts.size())
                data_posts.add(index, ad);
            if((offset % 2 == 0)) {}
            else{offset +=1;}
            index = index + offset;
            Log.e("OFFSEET_aDD","-->" + index);
        }
        adapter.setPosts(data_posts);
    }

    private void insertAdsInMenuItemsBelow(boolean more,ArrayList<PostBean> data_parameter) {
        loading = true;
        ArrayList<Object> data = new ArrayList<>();
        data.addAll(data_parameter);
        mNativeAds.clear();
        mNativeAds.addAll(App.getInstance().getMoreAds());
        if (mNativeAds.size() <= 0) { return;}
        int offset = 9;
        int index = 9;
        for (UnifiedNativeAd ad: mNativeAds) {
            if(index< data.size())
                data.add(index, ad);
            if((offset % 2 == 0)) {}
            else{offset +=1;}
            index = index + offset;
            Log.e("OFFSEET_aDD","-->" + index);
        }
        adapter.addMorePosts(data);
    }

    @Override
    public void showMorePost(ArrayList<PostBean> posts) {
        if(posts.size()>0) {
            this.data_posts.addAll(posts);
            Log.e("SETIE_DATA_POST", "--> " + posts.size());
            if (adapter != null) {
                    loading = true;
                    ArrayList<Object> data = new ArrayList<>();
                    data.addAll(posts);
                    adapter.addMorePosts(data);
            }
        }else{
            Toast.makeText(getContext(),"NO HAY MAS",Toast.LENGTH_LONG).show();
        }
    }
}


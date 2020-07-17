package com.bunizz.instapetts.fragments.post;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.profile.AdapterGridPostsProfile;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.web.parameters.PostActions;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPostGalery extends Fragment implements PostGaleryContract.View {
    @BindView(R.id.list_galery)
    RecyclerView list_galery;


    @BindView(R.id.root_no_data)
    RelativeLayout root_no_data;

    @BindView(R.id.title_no_data)
    TextView title_no_data;

    @BindView(R.id.body_no_data)
    TextView body_no_data;

    PostGaleryPresenter presenter;

    changue_fragment_parameters_listener listener;
    AdapterGridPostsProfile feedAdapter;
    ArrayList<Object> data_posts = new ArrayList<>();
    private boolean loading =true;
    private boolean IS_ALL = false;

    int PAGINADOR = -999;


    public void setData_posts(ArrayList<Object> data_posts) {
        IS_ALL = false;

        if(data_posts.size()>0) {
            PostBean ID_POST = (PostBean)data_posts.get(data_posts.size() - 1);
            PAGINADOR = ID_POST.getId_post_from_web();
        }else{
            IS_ALL = true;
        }
        if(data_posts !=null){
            if(data_posts.size()>0){
                this.data_posts.clear();
                this.data_posts.addAll(data_posts);
                if(feedAdapter!=null)
                    feedAdapter.setPosts(this.data_posts);
                if(root_no_data!=null)
                root_no_data.setVisibility(View.GONE);
            }else{
                this.data_posts.clear();
                feedAdapter.clear();
                feedAdapter.refresh();
                if(root_no_data!=null)
                root_no_data.setVisibility(View.VISIBLE);
            }
        }else{
            if(root_no_data!=null)
               root_no_data.setVisibility(View.VISIBLE);
        }

    }




    public boolean isDataAdded(){
        if(this.data_posts.size()>0)
            return  true;
        else
            return  false;
    }
    public static FragmentPostGalery newInstance() {
        return new FragmentPostGalery();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedAdapter = new AdapterGridPostsProfile(getContext());
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
        Log.e("ONVIEW_CREATED","galeryxxxxxxx");
        list_galery.setLayoutManager(new GridLayoutManager(getContext(),3));
        list_galery.setAdapter(feedAdapter);
        feedAdapter.setListener(new changue_fragment_parameters_listener() {
            @Override
            public void change_fragment_parameter(int type_fragment, Bundle data) {
                ArrayList<Object> object_currents = new ArrayList<>();
                int position = data.getInt("POSITION");
                for (int i = position;i<data_posts.size();i++){
                    object_currents.add(data_posts.get(i));
                }
                Bundle b = new Bundle();
                b.putParcelable("POSTS", Parcels.wrap(object_currents));
                listener.change_fragment_parameter(type_fragment,b);
            }
        });
        feedAdapter.setListener_post(new postsListener() {
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
            public void commentPost(int id_post, boolean can_comment) {

            }
        });
        title_no_data.setText("No hay publicaciones");
        body_no_data.setText("Demuestrale al mundo la mascota linda que tienes escondida, todos queremos verla¡¡.");
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
                                Log.e("DONWLOAD_MORE_COMMENTS","SI");
                                presenter.getMorePost(3,PAGINADOR);
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

    }

    @Override
    public void showMorePost(ArrayList<PostBean> posts) {
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
                data_posts.clear();
                data_posts.addAll(posts);
                if(feedAdapter!=null)
                    feedAdapter.setPostsPaginate(this.data_posts);
                if(root_no_data!=null)
                    root_no_data.setVisibility(View.GONE);
            }
        }
    }
}


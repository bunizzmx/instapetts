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



    public void setData_posts(ArrayList<Object> data_posts) {
        if(data_posts !=null){
            if(data_posts.size()>0){
                Log.e("DATAPOST_GRID","DATA POS menor a 0 ");
                this.data_posts = data_posts;
                if(feedAdapter!=null)
                    feedAdapter.setPosts(this.data_posts);
                if(root_no_data!=null)
                root_no_data.setVisibility(View.GONE);
            }else{
                this.data_posts.clear();
                feedAdapter.refresh();
                Log.e("DATAPOST_GRID","DATA POS mayor 0");
                if(root_no_data!=null)
                root_no_data.setVisibility(View.VISIBLE);
            }
        }else{
            Log.e("DATAPOST_GRID","DATA POS NULL");
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
        Log.e("ONVIEW_CREATED","galery");
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
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;

    }

    @Override
    public void showMorePost(ArrayList<PostBean> posts) {

    }
}


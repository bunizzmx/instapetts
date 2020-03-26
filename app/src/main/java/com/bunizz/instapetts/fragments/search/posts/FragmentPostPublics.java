package com.bunizz.instapetts.fragments.search.posts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.searchqr.QrSearchActivity;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.web.CONST;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentPostPublics  extends Fragment implements  PostPublicsContract.View {


    @BindView(R.id.list_posts_publics)
    RecyclerView list_posts_publics;

    ArrayList<Object> data_posts = new ArrayList<>();


    @SuppressLint("MissingPermission")
    @OnClick(R.id.root_search_pets_users)
    void root_search_pets_users()
    {
      listener.change_fragment_parameter(FragmentElement.INSTANCE_SEARCH,null);
    }

    String URL_UPDATED="INVALID";
    String URL_LOCAL="INVALID";

    postsPublicsAdapter adapter;
    PostPublicsPresenter presenter;

    @BindView(R.id.search_by_qr)
    RelativeLayout search_by_qr;
    private static final int REQUEST_CODE_QR_SCAN = 101;

    changue_fragment_parameters_listener listener;
    RxPermissions rxPermissions;
    public static FragmentPostPublics newInstance() {
        return new FragmentPostPublics();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new postsPublicsAdapter(getContext());
        presenter = new PostPublicsPresenter(this,getContext());
        rxPermissions = new RxPermissions(getActivity());
        adapter.setListener(new changue_fragment_parameters_listener() {
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_publics, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_posts_publics.setLayoutManager(new GridLayoutManager(getContext(),3));
        list_posts_publics.setAdapter(adapter);
        presenter.getPostPublics();
        search_by_qr.setOnClickListener(view1 -> {
            rxPermissions
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA)
                    .subscribe(granted -> {
                        if (granted) {
                            Intent i = new Intent(getContext() , QrSearchActivity.class);
                            startActivityForResult( i,REQUEST_CODE_QR_SCAN);
                        } else {
                            App.getInstance().show_dialog_permision(getActivity(),getActivity().getResources().getString(R.string.permision_storage),
                                    getResources().getString(R.string.permision_storage_body),0);
                        }
                    });

        });
    }




    @Override
    public void showPosts(ArrayList<PostBean> posts) {
        data_posts.clear();
        data_posts.addAll(posts);
        adapter.setPosts(data_posts);
    }


    public class postsPublicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<Object> posts = new ArrayList<>();

        Context context;

        changue_fragment_parameters_listener listener;

        public changue_fragment_parameters_listener getListener() {
            return listener;
        }

        public void setListener(changue_fragment_parameters_listener listener) {
            this.listener = listener;
        }

        public postsPublicsAdapter(Context context) {
            this.context = context;
        }

        public ArrayList<Object> getPosts() {
            return posts;
        }

        public void setPosts(ArrayList<Object> posts) {
            this.posts = posts;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_posts_simple,parent,false);
            return new postsPublicsHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            postsPublicsHolder h = (postsPublicsHolder) holder;
            PostBean data_parsed =(PostBean) posts.get(position);
            String splits[]  = data_parsed.getUrls_posts().split(",");
            if(splits.length == 1){
              h.multiple_images_posts.setVisibility(View.GONE);
            }else{
                h.multiple_images_posts.setVisibility(View.VISIBLE);
            }
            Glide.with(context).load(splits[0]).into(h.fisrt_stack_foto);
            h.root_posts_search_item.setOnClickListener(view -> {
                if(listener!=null){
                    Bundle data_selected = new Bundle();
                    data_selected.putInt("POSITION",position);
                    listener.change_fragment_parameter(FragmentElement.INSTANCE_GET_POSTS_PUBLICS_ADVANCED,data_selected);
                }
            });

        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

        public class postsPublicsHolder extends RecyclerView.ViewHolder{
            ImageView multiple_images_posts,fisrt_stack_foto;
            RelativeLayout root_posts_search_item;
            public postsPublicsHolder(@NonNull View itemView) {
                super(itemView);
                fisrt_stack_foto = itemView.findViewById(R.id.fisrt_stack_foto);
                multiple_images_posts = itemView.findViewById(R.id.multiple_images_posts);
                root_posts_search_item = itemView.findViewById(R.id.root_posts_search_item);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }
}

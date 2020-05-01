package com.bunizz.instapetts.fragments.search.posts;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.profile.AdapterGridPostsProfile;
import com.bunizz.instapetts.fragments.search.AdapterGridPosts;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;

import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentListGalery extends Fragment {
    @BindView(R.id.list_posts_publics)
    RecyclerView list_posts_publics;

    changue_fragment_parameters_listener listener;
    ArrayList<Object> data_posts = new ArrayList<>();

    AdapterGridPosts adapter;
    public void setData_posts(ArrayList<Object> data_posts) {
        if(data_posts !=null){
            if(data_posts.size()>0){
                this.data_posts = data_posts;
                Log.e("SETIE_DATA_POST","--> " + data_posts.size());
                if(adapter!=null) {
                    adapter.setPosts(this.data_posts);
                }
            }else{

            }
        }else{

        }

    }
    public static FragmentListGalery newInstance() {
        return new FragmentListGalery();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AdapterGridPosts(getContext());
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
        list_posts_publics.setLayoutManager(new GridLayoutManager(getContext(),2));
        list_posts_publics.setAdapter(adapter);
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }
}


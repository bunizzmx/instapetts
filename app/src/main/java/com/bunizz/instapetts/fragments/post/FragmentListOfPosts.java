package com.bunizz.instapetts.fragments.post;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.post.adapters.GaleryAdapter;
import com.bunizz.instapetts.fragments.post.adapters.PostsAdapter;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;

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

public class FragmentListOfPosts extends Fragment {
    @BindView(R.id.list_posts_publics_advanced)
    RecyclerView list_posts_publics_advanced;

    changue_fragment_parameters_listener listener;
    PostsAdapter feedAdapter;

    ArrayList<Object> data = new ArrayList<>();

    public static FragmentListOfPosts newInstance() {
        return new FragmentListOfPosts();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedAdapter = new PostsAdapter(getContext());
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
        feedAdapter.setData(data);
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
            if(feedAdapter!=null)
            feedAdapter.addData(data);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }
}


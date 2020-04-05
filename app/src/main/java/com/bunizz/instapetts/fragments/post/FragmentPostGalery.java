package com.bunizz.instapetts.fragments.post;

import android.content.Context;
import android.os.Bundle;
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
import com.bunizz.instapetts.fragments.profile.AdapterGridPostsProfile;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPostGalery extends Fragment {
    @BindView(R.id.list_galery)
    RecyclerView list_galery;


    @BindView(R.id.root_no_data)
    RelativeLayout root_no_data;

    @BindView(R.id.title_no_data)
    TextView title_no_data;

    @BindView(R.id.body_no_data)
    TextView body_no_data;

    changue_fragment_parameters_listener listener;
    AdapterGridPostsProfile feedAdapter;
    ArrayList<Object> data_posts = new ArrayList<>();
    public void setData_posts(ArrayList<Object> data_posts) {
        if(data_posts !=null){
            if(data_posts.size()>0){
                this.data_posts = data_posts;
                if(feedAdapter!=null)
                    feedAdapter.setPosts(this.data_posts);
                if(root_no_data!=null)
                root_no_data.setVisibility(View.GONE);
            }else{
                if(root_no_data!=null)
                root_no_data.setVisibility(View.VISIBLE);
            }
        }else{
            if(root_no_data!=null)
            root_no_data.setVisibility(View.VISIBLE);
        }

    }
    public static FragmentPostGalery newInstance() {
        return new FragmentPostGalery();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedAdapter = new AdapterGridPostsProfile(getContext());

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
        title_no_data.setText("No hay publicaciones");
        body_no_data.setText("Demuestrale al mundo la mascota linda que tienes escondida, todos queremos verla¡¡.");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }
}


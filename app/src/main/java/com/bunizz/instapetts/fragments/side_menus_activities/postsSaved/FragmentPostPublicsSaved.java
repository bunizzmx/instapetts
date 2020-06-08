package com.bunizz.instapetts.fragments.side_menus_activities.postsSaved;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.searchqr.QrSearchActivity;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.search.AdapterGridPosts;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;
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

public class FragmentPostPublicsSaved extends Fragment implements  PostPublicsSavedContract.View {


    @BindView(R.id.list_posts_publics)
    RecyclerView list_posts_publics;

    @BindView(R.id.title_no_data)
    TextView title_no_data;

    @BindView(R.id.body_no_data)
    TextView body_no_data;

    @BindView(R.id.root_no_data)
    RelativeLayout root_no_data;

    @BindView(R.id.progres_image)
    SpinKitView progres_image;


    ArrayList<Object> data_posts = new ArrayList<>();

    String URL_UPDATED="INVALID";
    String URL_LOCAL="INVALID";

    AdapterGridPosts adapter;
    PostPublicsSavedPresenter presenter;
    int IS_FORM_SAVED_POST =0;

    private static final int REQUEST_CODE_QR_SCAN = 101;

    changue_fragment_parameters_listener listener;
    RxPermissions rxPermissions;
    public static FragmentPostPublicsSaved newInstance() {
        return new FragmentPostPublicsSaved();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            IS_FORM_SAVED_POST = bundle.getInt("SAVED_POST");
        }
        adapter = new AdapterGridPosts(getContext());
        presenter = new PostPublicsSavedPresenter(this,getContext());
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
        return inflater.inflate(R.layout.fragment_post_publics_saved, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_posts_publics.setLayoutManager(new GridLayoutManager(getContext(),3));
        list_posts_publics.setAdapter(adapter);
        presenter.getPostPublics();
        Style style = Style.values()[12];
        Sprite drawable = SpriteFactory.create(style);
        progres_image.setIndeterminateDrawable(drawable);
        progres_image.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        title_no_data.setText(getContext().getResources().getString(R.string.no_saved_post_title));
        body_no_data.setText(getString(R.string.no_saved_post_title));
    }




    @Override
    public void showPosts(ArrayList<PostBean> posts) {
        if(posts!=null){
            if(posts.size()>0){
                progres_image.setVisibility(View.GONE);
                root_no_data.setVisibility(View.GONE);
                data_posts.clear();
                data_posts.addAll(posts);
                adapter.setPosts(data_posts);
            }else{
                progres_image.setVisibility(View.GONE);
                root_no_data.setVisibility(View.VISIBLE);
            }
        }else{
            progres_image.setVisibility(View.GONE);
            root_no_data.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void Error() {
        root_no_data.setVisibility(View.VISIBLE);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }
}

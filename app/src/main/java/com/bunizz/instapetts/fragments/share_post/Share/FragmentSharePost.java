package com.bunizz.instapetts.fragments.share_post.Share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.fragments.feed.FeedPresenter;
import com.bunizz.instapetts.utils.dilogs.DialogShosePet;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSharePost extends Fragment implements  SharePostContract.View{



    @BindView(R.id.list_image_selected)
    RecyclerView list_image_selected;

    @BindView(R.id.location_user)
    TextView location_user;


    @OnClick(R.id.share_post_pet)
    void share_post_pet()
    {
        DialogShosePet dialogShosePet = new DialogShosePet(getActivity());
        dialogShosePet.show();
    }





    ArrayList<String> paths = new ArrayList<>();
    SharePostPresenter presenter;

    ListSelectedAdapter adapter;
    public static FragmentSharePost newInstance() {
        return new FragmentSharePost();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ListSelectedAdapter(getContext());
        presenter = new SharePostPresenter(this, getContext());
        Bundle bundle=getArguments();
        if(bundle!=null){
            paths.addAll(bundle.getStringArrayList("data_pahs"));
        }
        adapter.setData(paths);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_image_selected.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        list_image_selected.setAdapter(adapter);
        presenter.getLocation();
    }

    @Override
    public void showLocation(String corrdenadas) {
        location_user.setText(corrdenadas);
    }
}


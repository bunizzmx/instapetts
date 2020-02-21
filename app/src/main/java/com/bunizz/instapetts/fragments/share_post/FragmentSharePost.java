package com.bunizz.instapetts.fragments.share_post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.fragments.info.InfoPetFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class FragmentSharePost extends Fragment implements  FeedContract.View{


    public static FragmentSharePost newInstance() {
        return new FragmentSharePost();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_imagepicker_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }


    @Override
    public void show_feed(ArrayList<PetBean> data) {

    }
}


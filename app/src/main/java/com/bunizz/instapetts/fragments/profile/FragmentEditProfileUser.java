package com.bunizz.instapetts.fragments.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.open_sheet_listener;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.utils.tabs.TabType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentEditProfileUser extends Fragment {

    @SuppressLint("MissingPermission")
    @OnClick(R.id.change_photo)
    void change_photo()
    {
       listener.onImageProfileUpdated();
    }

    @BindView(R.id.image_userd_edit)
    ImageView image_userd_edit;


    uploads listener;

    public static FragmentEditProfileUser newInstance() {
        return new FragmentEditProfileUser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }


    public void change_image_profile(String url){
        Glide.with(getContext()).load(url).into(image_userd_edit);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (uploads) context;
    }
}

package com.bunizz.instapetts.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.listeners.change_instance;
import butterknife.ButterKnife;

public class FragmentDataUser extends Fragment {

    change_instance listener;

    public static FragmentDataUser newInstance() {
        return new FragmentDataUser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
    }
}


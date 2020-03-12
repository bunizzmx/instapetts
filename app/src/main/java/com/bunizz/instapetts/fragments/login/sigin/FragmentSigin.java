package com.bunizz.instapetts.fragments.login.sigin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.login_listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSigin extends Fragment {

    change_instance listener;
    login_listener login_listener;
    @OnClick(R.id.back_to_main)
    void back_to_main()
    {
        listener.onback();
    }

    @OnClick(R.id.button_signin)
    void button_signin()
    {
        if(login_listener!=null)
         login_listener.loginWithEmail("aponce.rioroodfk@gmail.com","adokdsgfdetete546546g");
    }



    public static FragmentSigin newInstance() {
        return new FragmentSigin();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sigin, container, false);
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
        login_listener= (login_listener) context;
    }
}

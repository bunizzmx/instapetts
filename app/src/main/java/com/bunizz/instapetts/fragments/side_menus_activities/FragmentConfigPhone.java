package com.bunizz.instapetts.fragments.side_menus_activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.searchqr.QrSearchActivity;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentConfigPhone extends Fragment {


    @SuppressLint("MissingPermission")
    @OnClick(R.id.pick_code_country)
    void pick_code_country() {
        listener.change_fragment_parameter(FragmentElement.INSTANCE_COUNTRY_CODES,null);
    }

    changue_fragment_parameters_listener listener;

    public static FragmentConfigPhone newInstance() {
        return new FragmentConfigPhone();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config_phone, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener =(changue_fragment_parameters_listener)context;
    }
}

package com.bunizz.instapetts.fragments.tips;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.tips.adapters.TipsAdapter;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentTipDetail extends Fragment {

    change_instance listener;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.back_to_main)
    void back_to_main() {
        listener.change(FragmentElement.INSTANCE_TIPS);
    }

    public static FragmentTipDetail newInstance() {
        return new FragmentTipDetail();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice_tip, container, false);
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


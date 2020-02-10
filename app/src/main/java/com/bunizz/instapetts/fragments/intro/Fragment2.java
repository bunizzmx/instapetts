package com.bunizz.instapetts.fragments.intro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bunizz.instapetts.R;
import com.bunizz.instapetts.listeners.VisibleItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;


public class Fragment2 extends Fragment {
    @BindView(R.id.title_fragment_1)
    TextView title_fragment_1;

    @BindView(R.id.body_fragment_2)
    TextView body_fragment_1;

    @BindView(R.id.progress_politicas)
    ProgressBar progress_politicas;

    FirebaseFirestore db;
    VisibleItem interfaz;

    @BindView(R.id.layout_politicas)
    RelativeLayout layout_politicas;

    @BindView(R.id.politics)
    TextView politics;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            interfaz = (VisibleItem) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e.getMessage());
        }
    }

}

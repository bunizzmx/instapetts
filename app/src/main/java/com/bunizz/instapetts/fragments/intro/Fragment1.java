package com.bunizz.instapetts.fragments.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Fragment1 extends Fragment {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.desc)
    TextView desc;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment1, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        //desc.setText(getContext().getResources().getString(R.string.body_fragment_1));
        //desc.setTypeface(App.monserrat_black);


    }
}

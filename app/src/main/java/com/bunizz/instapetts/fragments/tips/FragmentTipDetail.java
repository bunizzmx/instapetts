package com.bunizz.instapetts.fragments.tips;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
    String body_tip="";
    String title_tip="";
    int id=0;
    String url="";

    @BindView(R.id.body_detail)
    TextView body_detail;

    @BindView(R.id.title_detail)
    TextView title_detail;

    @BindView(R.id.image_tip_detail)
    ImageView image_tip_detail;


    @BindView(R.id.like_tip)
    ImageView like_tip;
    boolean is_like=false;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.back_to_main)
    void back_to_main() {
        listener.change(FragmentElement.INSTANCE_TIPS);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.like_tip)
    void like_tip() {
        if(is_like) {
            is_like =false;
            like_tip.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_corazon));
        }else{
            is_like =true;
            like_tip.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_corazon_black));
        }
    }



    public static FragmentTipDetail newInstance() {
        return new FragmentTipDetail();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            body_tip = bundle.getString("BODY_TIP");
            url = bundle.getString("PHOTO_TIP");
            title_tip =bundle.getString("TITLE_TIP");
            id  = bundle.getInt("ID_TIP");
        }
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
        body_detail.setText(body_tip);
        title_detail.setText(title_tip);
        Glide.with(getContext()).load(url).into(image_tip_detail);
    }

    public void refill_data(Bundle data){
        if(body_detail!=null) {
            if (data != null) {
                body_tip = data.getString("BODY_TIP");
                title_tip =data.getString("TITLE_TIP");
                url = data.getString("PHOTO_TIP");
                id  = data.getInt("ID_TIP");
                body_detail.setText(body_tip);
                title_detail.setText(title_tip);
                Glide.with(getContext()).load(url).into(image_tip_detail);
            }
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
    }
}


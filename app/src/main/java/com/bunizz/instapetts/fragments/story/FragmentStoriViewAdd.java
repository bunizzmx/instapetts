package com.bunizz.instapetts.fragments.story;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.listeners.delete;
import com.bunizz.instapetts.listeners.story_finished_listener;
import com.bunizz.instapetts.utils.HistoryView.StoryPlayerProgressView;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.dilogs.DialogDeletes;
import com.bunizz.instapetts.utils.double_tap.DoubleTapLikeView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.parceler.Parcels;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentStoriViewAdd extends Fragment {


    long pressTime = 0L;
    long limit = 500L;
    int PROGRESS_COUNT = 0;
    int COUNTER =0;



    story_finished_listener listener;

    ArrayList<IndividualDataPetHistoryBean> uris_fotos = new ArrayList<>();
    HistoriesBean HISTORY_BEAN;
    String unparseableStories="";
    UnifiedNativeAd  nativeAd ;

   boolean AUTOPLAY =false;
    public static FragmentStoriViewAdd newInstance() {
        return new FragmentStoriViewAdd();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null) {
            try {
                nativeAd = (UnifiedNativeAd) convertFromBytes(bundle.getByteArray("ADS"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ad_unified_story, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


    }




    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle=getArguments();
        if(bundle!=null)
            AUTOPLAY = bundle.getBoolean("AUTOPLAY");

    }

    @Override
    public void onPause() {
        super.onPause();
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

}

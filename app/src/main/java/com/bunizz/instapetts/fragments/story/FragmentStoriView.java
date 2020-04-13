package com.bunizz.instapetts.fragments.story;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.post.FragmentPostList;
import com.bunizz.instapetts.fragments.post.adapters.ListAdapter;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.story_finished_listener;
import com.bunizz.instapetts.utils.HistoryView.StoryPlayer;
import com.bunizz.instapetts.utils.HistoryView.StoryPlayerProgressView;
import com.bunizz.instapetts.utils.ImagenCircular;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentStoriView extends Fragment implements  StoryPlayerProgressView.StoryPlayerListener{


    long pressTime = 0L;
    long limit = 500L;
    int PROGRESS_COUNT = 0;
    int COUNTER =0;

    @BindView(R.id.image_story)
    ImageView image_story;
    @BindView(R.id.usuario_historia_nombre)
    TextView name;
    @BindView(R.id.fecha_expresion)
    TextView time;
    @BindView(R.id.progressBarView)
    StoryPlayerProgressView storyPlayerProgressView;

    @BindView(R.id.progress_top)
    ProgressBar progress_top;

    @BindView(R.id.imagen_usuario_historia)
    ImagenCircular imagen_usuario_historia;



    story_finished_listener listener;

    ArrayList<IndividualDataPetHistoryBean> uris_fotos = new ArrayList<>();
    HistoriesBean HISTORY_BEAN;

    public static FragmentStoriView newInstance() {
        return new FragmentStoriView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null) {
            HISTORY_BEAN =  Parcels.unwrap(bundle.getParcelable("HISTORY_PARAMETER"));
            uris_fotos = HISTORY_BEAN.getHistories();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_story_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        storyPlayerProgressView.setSingleStoryDisplayTime(6000);
        PROGRESS_COUNT=uris_fotos.size();
        initStoryProgressView();
        name.setText(HISTORY_BEAN.getName_user());
        Glide.with(getActivity()).load(HISTORY_BEAN.getUrl_photo_user()).into(imagen_usuario_historia);
    }

    private void initStoryProgressView() {
        storyPlayerProgressView.setStoryPlayerListener(this);
        storyPlayerProgressView.setProgressBarsCount(PROGRESS_COUNT);
        setTouchListener();
    }

    @Override
    public void onStartedPlaying(int index) {
        loadImage(index);
    }

    @Override
    public void onFinishedPlaying() {
        if(listener!=null){
            listener.on_finish();
        }
        Log.e("EEROR_GLIDE","FINISH");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("EEROR_GLIDE","ON RESUME");
        storyPlayerProgressView.resumeProgress();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("EEROR_GLIDE","ON PAUSE");
        storyPlayerProgressView.pauseProgress();
    }

    private void loadImage(int index) {
        COUNTER =index;
        if (getActivity() == null) {
            Log.e("AQUI_TOMNE","si");
            return;
        }

        try {
            time.setText(App.fecha_lenguaje_humano(uris_fotos.get(COUNTER).getDate_story().replace("T"," ").replace("Z","")));
        }catch (Exception e){
            time.setText("Hace un momento");
        }

        Glide.with(getContext()).asBitmap().load(uris_fotos.get(index).getUrl_photo())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            progress_top.setVisibility(View.GONE);
                            Log.e("SCALE_HEIGHT","-->" +resource.getHeight() );
                            if(resource.getHeight() > 1200){
                                image_story.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }else{
                                image_story.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                            image_story.setImageBitmap(resource);
                        }catch (Exception e){
                            Log.e("EEROR_GLIDE","-->" + e.getMessage());
                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener() {
        image_story.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                //pause
                storyPlayerProgressView.pauseProgress();
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                //resume
                storyPlayerProgressView.resumeProgress();
                return true;
            }else {
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (story_finished_listener) context;
    }

}

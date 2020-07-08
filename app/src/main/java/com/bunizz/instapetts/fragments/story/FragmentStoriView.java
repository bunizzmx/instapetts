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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;

import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.delete;
import com.bunizz.instapetts.listeners.story_finished_listener;
import com.bunizz.instapetts.utils.HistoryView.StoryPlayerProgressView;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.dilogs.DialogDeletes;
import com.bunizz.instapetts.utils.double_tap.DoubleTapLikeView;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.info_of_likes_views)
    LinearLayout info_of_likes_views;


    @BindView(R.id.delete_history)
    LinearLayout delete_history;

    @BindView(R.id.like_history_layout)
    RelativeLayout like_history_layout;

    @BindView(R.id.icon_like_history)
    ImageView icon_like_history;

    @BindView(R.id.layout_double_tap_like)
    DoubleTapLikeView layout_double_tap_like;


    @BindView(R.id.imagen_mascota_history)
    ImagenCircular imagen_mascota_history;

    @BindView(R.id.name_mascota_history)
    TextView name_mascota_history;

    @BindView(R.id.num_likes_story)
    TextView num_likes_story;


    @BindView(R.id.num_views_story)
    TextView num_views_story;


    @OnClick(R.id.delete_history)
    void delete_history()
    {
        storyPlayerProgressView.pauseProgress();
        DialogDeletes delete_pet = new DialogDeletes(getContext(),0,4);
        delete_pet.setListener(new delete() {
            @Override
            public void delete(boolean delete) {
                if(delete) {
                    Toast.makeText(getContext(),"Historia Eliminada",Toast.LENGTH_LONG).show();
                    listener.onItemDeleted(uris_fotos.get(COUNTER).getIdentificador());
                    getActivity().onBackPressed();
                }else{
                    storyPlayerProgressView.resumeProgress();
                }
              //  presenter.delete(Integer.parseInt(petBean.getId_pet()));
            }
            @Override
            public void deleteOne(int id) {

            }
        });
        delete_pet.show();
    }

    story_finished_listener listener;

    ArrayList<IndividualDataPetHistoryBean> uris_fotos = new ArrayList<>();
    HistoriesBean HISTORY_BEAN;
    String unparseableStories="";

   boolean AUTOPLAY =false;
    public static FragmentStoriView newInstance() {
        return new FragmentStoriView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null) {
            HISTORY_BEAN =  Parcels.unwrap(bundle.getParcelable("HISTORY_PARAMETER"));
            unparseableStories = HISTORY_BEAN.getHistorias();
            AUTOPLAY = bundle.getBoolean("AUTOPLAY");
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
        Log.e("HISTORIES_BEAN","--->:::::" + unparseableStories);
        storyPlayerProgressView.cancelAnimation();

        if(HISTORY_BEAN.getId_user() == App.read(PREFERENCES.ID_USER_FROM_WEB,0)) {
            like_history_layout.setVisibility(View.GONE);
            info_of_likes_views.setVisibility(View.VISIBLE);
            delete_history.setVisibility(View.VISIBLE);
        }
        else {
            delete_history.setVisibility(View.GONE);
            like_history_layout.setVisibility(View.VISIBLE);
            info_of_likes_views.setVisibility(View.GONE);
            like_history_layout.setOnClickListener(v -> {
                listener.onItemLiked(uris_fotos.get(COUNTER).getIdentificador(),HISTORY_BEAN.getId_user());
                layout_double_tap_like.setVisibility(View.VISIBLE);
                layout_double_tap_like.animate_icon(layout_double_tap_like);
                icon_like_history.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_corazon_black));
            });
        }


        String splitsItems[] = unparseableStories.split(",");
        for(int i=0;i<=splitsItems.length - 1;i++){
           String splitsSubItems[] = splitsItems[i].split(";");
           IndividualDataPetHistoryBean item = new IndividualDataPetHistoryBean();
            item.setName_pet(splitsSubItems[0]);
            item.setPhoto_pet(splitsSubItems[1]);
            item.setId_pet(Integer.parseInt(splitsSubItems[2]));
            item.setTumbh_video(splitsSubItems[3]);
            item.setUrl_photo(splitsSubItems[4]);
            item.setIdentificador(splitsSubItems[5]);
            item.setDate_story(splitsSubItems[6]);
           uris_fotos.add(item);
        }
        PROGRESS_COUNT=uris_fotos.size();

        name.setText(HISTORY_BEAN.getName_user());
        Glide.with(getActivity()).load(HISTORY_BEAN.getPhoto_user()).into(imagen_usuario_historia);

    }

    public void startProgressAnimation(){
        Log.e("START_ANIMATION","SI");
        initStoryProgressView();
        storyPlayerProgressView.resumeProgress();
    }

    public void StopProgressAnimation(){
        if(storyPlayerProgressView!=null)
         storyPlayerProgressView.pauseProgress();
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
        Bundle bundle=getArguments();
        if(bundle!=null)
            AUTOPLAY = bundle.getBoolean("AUTOPLAY");

        if(AUTOPLAY) {
            Log.e("AUTOPLAY","SI");
            startProgressAnimation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void loadImage(int index) {
        COUNTER =index;
        if (getActivity() == null) {
            Log.e("AQUI_TOMNE","si");
            return;
        }

        if(HISTORY_BEAN.getId_user() == App.read(PREFERENCES.ID_USER_FROM_WEB,0)) {
            IdentificadoresHistoriesBean ide  = listener.getIdenTificador(uris_fotos.get(COUNTER).getIdentificador());
            int num_likes = ide.getNum_likes();
            num_likes_story.setText("" + num_likes);
            int num_views = ide.getNum_views();
            num_views_story.setText("" + num_views);
        }
        else
            info_of_likes_views.setVisibility(View.GONE);

        Glide.with(getActivity()).load(uris_fotos.get(COUNTER).getPhoto_pet()).into(imagen_mascota_history);
        name_mascota_history.setText(uris_fotos.get(COUNTER).getName_pet());

        listener.onItemView(uris_fotos.get(index).getIdentificador(),HISTORY_BEAN.getId_user());
        try {
            time.setText(App.getInstance().fecha_lenguaje_humano(uris_fotos.get(COUNTER).getDate_story().replace("T"," ").replace("Z","")));
        }catch (Exception e){
            time.setText(getContext().getResources().getString(R.string.now));
        }

        Glide.with(getContext()).asBitmap().load(App.getInstance().getBucketUriHistorie(uris_fotos.get(index).getUrl_photo()))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            progress_top.setVisibility(View.GONE);
                            Log.e("SCALE_HEIGHT","-->" +resource.getHeight() );
                            Log.e("SCALE_WIDTH","-->" +resource.getWidth() );
                            if(resource.getHeight() > 900){
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

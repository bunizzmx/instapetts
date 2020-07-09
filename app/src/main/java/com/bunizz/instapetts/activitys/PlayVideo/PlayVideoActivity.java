package com.bunizz.instapetts.activitys.PlayVideo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.reports.ReportActiviy;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.TipsBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.actions_dialog_profile;
import com.bunizz.instapetts.utils.AnalogTv.AnalogTvNoise;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.dilogs.DialogOptionsPosts;
import com.bunizz.instapetts.utils.slidemenu.SlideMenuLayout;
import com.bunizz.instapetts.utils.video_player.ExoPlayerRecyclerView;
import com.bunizz.instapetts.utils.video_player.PreviewTimeBar;
import com.bunizz.instapetts.utils.video_player.PreviewTumbh.ExoPlayerManager;
import com.bunizz.instapetts.utils.video_player.PreviewView;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;


import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.http.POST;

public class PlayVideoActivity extends AppCompatActivity implements PreviewView.OnPreviewChangeListener,PlayVideoContract.View {


    @BindView(R.id.exoplayer_play_video)
    PlayerView videoSurfaceView;

    @BindView(R.id.title_video_play)
    TextView title_video_play;

    @BindView(R.id.name_user_player)
    TextView name_user_player;

    @BindView(R.id.likes_video)
    TextView likes_video;

    @BindView(R.id.views_video)
    TextView views_video;


    @BindView(R.id.info_video)
    LinearLayout info_video;

    @BindView(R.id.touch_view_more_controls)
    LinearLayout touch_view_more_controls;

    @BindView(R.id.image_video_tumbh)
    ImagenCircular image_video_tumbh;

    @BindView(R.id.layout_views)
    LinearLayout layout_views;

    @BindView(R.id.noise)
    AnalogTvNoise noise;

    @BindView(R.id.icon_like_video)
    ImageView icon_like_video;


    @BindView(R.id.icon_save_on_favorites)
    ImageView icon_save_on_favorites;

    @BindView(R.id.layout_censored)
    RelativeLayout layout_censored;

    @BindView(R.id.open_dialog_options)
    RelativeLayout open_dialog_options;


    @SuppressLint("MissingPermission")
    @OnClick(R.id.like_video_post)
    void like_video_post() {
        PostActions postActions = new PostActions();
        postActions.setId_post(POST_BEAN.getId_post_from_web());
        if(!POST_BEAN.isLiked()){
            POST_BEAN.setLiked(true);
            postActions.setAcccion("1");
        }
        else {
            POST_BEAN.setLiked(false);
            postActions.setAcccion("2");
        }
        postActions.setId_usuario(POST_BEAN.getId_usuario());
        postActions.setValor("1");
        postActions.setExtra(POST_BEAN.getThumb_video());

        if(POST_BEAN.isLiked())
            icon_like_video.setImageDrawable(getResources().getDrawable(R.drawable.ic_corazon_w));
        else
            icon_like_video.setImageDrawable(getResources().getDrawable(R.drawable.ic_corazon_black));

        presenter.likeVideo(postActions);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.open_dialog_options)
    void open_dialog_options() {
        DialogOptionsPosts optionsPosts = new DialogOptionsPosts(this,POST_BEAN.getId_post_from_web(),POST_BEAN.getId_usuario(),POST_BEAN.getUuid());
        optionsPosts.setListener(new actions_dialog_profile() {
            @Override
            public void delete_post(int id_post) {
                PostBean postBean = new PostBean();
                postBean.setId_post_from_web(id_post);
                postBean.setId_usuario(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
                postBean.setTarget(WEBCONSTANTS.DELETE);
                presenter.deleteVideo(postBean);
            }

            @Override
            public void reportPost(int id_post) {
                Intent reportIntent = new Intent(PlayVideoActivity.this, ReportActiviy.class);
                reportIntent.putExtra("ID_RECURSO",id_post);
                reportIntent.putExtra("TYPO_RECURSO",1);
                startActivity(reportIntent);
            }
            @Override
            public void unfollowUser(int id_user,String uuid) {
                presenter.unfollowUser(uuid,id_user);
            }
        });
        optionsPosts.show();
    }





    @SuppressLint("MissingPermission")
    @OnClick(R.id.save_on_favorites)
    void save_on_favorites() {
        PostActions postActions = new PostActions();
        postActions.setId_post(POST_BEAN.getId_post_from_web());
        if(!POST_BEAN.isSaved()){
            POST_BEAN.setSaved(true);
            postActions.setAcccion("1");
        }
        else {
            POST_BEAN.setSaved(false);
            postActions.setAcccion("2");
        }

        postActions.setId_usuario(POST_BEAN.getId_usuario());
        postActions.setValor("1");
        postActions.setExtra(POST_BEAN.getThumb_video());

        if(presenter.isSaved(POST_BEAN.getId_post_from_web()))
            icon_save_on_favorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_w));
        else
            icon_save_on_favorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_fill));

        if(POST_BEAN.isSaved())
            presenter.saveFavorite(postActions, POST_BEAN);
        else
            presenter.deleteFavorite(POST_BEAN.getId_post_from_web());
    }



    private ExoPlayerManager exoPlayerManager;
    TipsBean TIPS_BEAN;
    PostBean POST_BEAN;
    private PreviewTimeBar previewTimeBar;
    int TYPE_PLAYER=0;
    PlayVideoPresenter presenter;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);
        presenter = new PlayVideoPresenter(this,this);
        if (getIntent() != null) {
            TYPE_PLAYER = getIntent().getIntExtra("TYPE_PLAYER",0);
            if(TYPE_PLAYER == 0)
                TIPS_BEAN = Parcels.unwrap(getIntent().getParcelableExtra("BEAN"));
            else
                POST_BEAN = Parcels.unwrap(getIntent().getParcelableExtra("BEAN"));

        }

        if(TYPE_PLAYER == 1) {
            if (presenter.isLiked(POST_BEAN.getId_post_from_web()))
                POST_BEAN.setLiked(true);
            else
                POST_BEAN.setLiked(false);


            if (presenter.isSaved(POST_BEAN.getId_post_from_web()))
                icon_save_on_favorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_fill));
            else
                icon_save_on_favorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_w));


            if (POST_BEAN.isLiked())
                icon_like_video.setImageDrawable(getResources().getDrawable(R.drawable.ic_corazon_black));
            else
                icon_like_video.setImageDrawable(getResources().getDrawable(R.drawable.ic_corazon_w));
        }else{
            icon_save_on_favorites.setVisibility(View.GONE);
            open_dialog_options.setVisibility(View.GONE);
        }

        changeStatusBarColor(R.color.background_video_color);
        if(TYPE_PLAYER ==0) {
            title_video_play.setText(TIPS_BEAN.getTitle_tip());
            likes_video.setText("" + TIPS_BEAN.getLikes_tip());
            views_video.setText("" + TIPS_BEAN.getViews_tip());
            init_timeline();
        }else{
            title_video_play.setText(POST_BEAN.getDescription());
            likes_video.setText("" + POST_BEAN.getLikes());
            if(POST_BEAN.getCensored() == 0)
                init_timeline();
            else {
                layout_censored.setVisibility(View.VISIBLE);
                touch_view_more_controls.setVisibility(View.GONE);
            }
        }

             if(TYPE_PLAYER == 0) {
                 name_user_player.setText("" + getString(R.string.instapetts_tips));
                 Glide.with(PlayVideoActivity.this).load(TIPS_BEAN.getPhoto_tumbh_tip()).into(image_video_tumbh);
             }
             else {
                 name_user_player.setText(POST_BEAN.getName_user());
                 Glide.with(PlayVideoActivity.this).load(POST_BEAN.getUrl_photo_user()).into(image_video_tumbh);
             }
    }

    @SuppressLint("ClickableViewAccessibility")
    void init_timeline(){

        previewTimeBar = videoSurfaceView.findViewById(R.id.exo_progress);
        if(TYPE_PLAYER == 0){
            Glide.with(PlayVideoActivity.this).load(TIPS_BEAN.getPhoto_tumbh_tip()).into(image_video_tumbh);
            exoPlayerManager = new ExoPlayerManager(this,videoSurfaceView, previewTimeBar,
                    (ImageView) findViewById(R.id.imageView), TIPS_BEAN.getPhoto_tumbh_tip(),noise);
            exoPlayerManager.play(Uri.parse(TIPS_BEAN.getPhoto_tip()));
            name_user_player.setText("" + getString(R.string.instapetts_tips));
        }else{
            layout_views.setVisibility(View.GONE);
            name_user_player.setText(POST_BEAN.getName_user());
            Glide.with(PlayVideoActivity.this).load(POST_BEAN.getUrl_photo_user()).into(image_video_tumbh);
            exoPlayerManager = new ExoPlayerManager(this,videoSurfaceView, previewTimeBar,
                    (ImageView) findViewById(R.id.imageView), POST_BEAN.getThumb_video(),noise);
            exoPlayerManager.play(Uri.parse(POST_BEAN.getUrls_posts()));
            if(POST_BEAN.getAspect().equals("16_9"))
                videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            else
                videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        }

        videoSurfaceView.showController();
        videoSurfaceView.setControllerShowTimeoutMs(5000);
        videoSurfaceView.setControllerVisibilityListener(visibility -> {
            if(visibility ==0){
                hide_controls();
            }else{
                show_controls();
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            Log.e("On Config Change","LANDSCAPE");
        }else{
            videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            Log.e("On Config Change","PORTRAIT");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(exoPlayerManager!=null)
          exoPlayerManager.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(exoPlayerManager!=null)
           exoPlayerManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(exoPlayerManager!=null)
          exoPlayerManager.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(exoPlayerManager!=null)
          exoPlayerManager.onStop();
    }


    @Override
    public void onStartPreview(PreviewView previewView, int progress) {
        if (getResources().getBoolean(R.bool.landscape)) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onStopPreview(PreviewView previewView, int progress) {
        if(exoPlayerManager!=null)
          exoPlayerManager.stopPreview(progress);
    }

    @Override
    public void onPreview(PreviewView previewView, int progress, boolean fromUser) {

    }


    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), color));
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), color));
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    private void show_controls() {
        info_video.bringToFront();
        info_video.animate().cancel();
        info_video.setAlpha(1f);
        info_video.animate()
                    .alpha(0f)
                    .setDuration(600);

        touch_view_more_controls.bringToFront();
        touch_view_more_controls.animate().cancel();
        touch_view_more_controls.setAlpha(1f);
        touch_view_more_controls.animate()
                .alpha(0f)
                .setDuration(600);
        }

    void hide_controls(){
        info_video.bringToFront();
        info_video.animate().cancel();
        info_video.setAlpha(0f);
        info_video.animate()
                .alpha(1f)
                .setDuration(600);

        touch_view_more_controls.bringToFront();
        touch_view_more_controls.animate().cancel();
        touch_view_more_controls.setAlpha(0f);
        touch_view_more_controls.animate()
                .alpha(1f)
                .setDuration(600);
    }


    @Override
    public void LikeSuccess() {

    }

    @Override
    public void deletePostError(boolean deleted) {
        Toast.makeText(this,"Publicacion eliminada",Toast.LENGTH_LONG).show();
        finish();
    }
}





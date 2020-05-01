package com.bunizz.instapetts.activitys.PlayVideo;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.TipsBean;
import com.bunizz.instapetts.utils.AnalogTv.AnalogTvNoise;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.slidemenu.SlideMenuLayout;
import com.bunizz.instapetts.utils.video_player.ExoPlayerRecyclerView;
import com.bunizz.instapetts.utils.video_player.PreviewTimeBar;
import com.bunizz.instapetts.utils.video_player.PreviewTumbh.ExoPlayerManager;
import com.bunizz.instapetts.utils.video_player.PreviewView;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;


import org.parceler.Parcels;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayVideoActivity extends AppCompatActivity implements PreviewView.OnPreviewChangeListener {


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

    private ExoPlayerManager exoPlayerManager;

    private static final String AppName = "Android ExoPlayer";
    private ExoPlayer exoPlayer;
    TipsBean TIPS_BEAN;
    PostBean POST_BEAN;
    private PreviewTimeBar previewTimeBar;
    int TYPE_PLAYER=0;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            TYPE_PLAYER = getIntent().getIntExtra("TYPE_PLAYER",0);
            if(TYPE_PLAYER == 0)
                TIPS_BEAN = Parcels.unwrap(getIntent().getParcelableExtra("BEAN"));
            else
                POST_BEAN = Parcels.unwrap(getIntent().getParcelableExtra("BEAN"));

        }
        changeStatusBarColor(R.color.background_video_color);
        if(TYPE_PLAYER ==0) {
            title_video_play.setText(TIPS_BEAN.getTitle_tip());
            likes_video.setText("" + TIPS_BEAN.getLikes_tip());
            views_video.setText("" + TIPS_BEAN.getViews_tip());
        }else{
            title_video_play.setText(POST_BEAN.getDescription());
            likes_video.setText("" + POST_BEAN.getLikes());
        }
        init_timeline();
    }

    @SuppressLint("ClickableViewAccessibility")
    void init_timeline(){

        previewTimeBar = videoSurfaceView.findViewById(R.id.exo_progress);
        if(TYPE_PLAYER == 0){
            Glide.with(PlayVideoActivity.this).load(TIPS_BEAN.getPhoto_tumbh_tip()).into(image_video_tumbh);
            exoPlayerManager = new ExoPlayerManager(this,videoSurfaceView, previewTimeBar,
                    (ImageView) findViewById(R.id.imageView), TIPS_BEAN.getPhoto_tumbh_tip(),noise);
            exoPlayerManager.play(Uri.parse(TIPS_BEAN.getPhoto_tip()));
            name_user_player.setText("INSTAPETS TIP");
        }else{
            layout_views.setVisibility(View.GONE);
            name_user_player.setText(POST_BEAN.getName_user());
            Glide.with(PlayVideoActivity.this).load(POST_BEAN.getUrl_photo_user()).into(image_video_tumbh);
            exoPlayerManager = new ExoPlayerManager(this,videoSurfaceView, previewTimeBar,
                    (ImageView) findViewById(R.id.imageView), POST_BEAN.getThumb_video(),noise);
            exoPlayerManager.play(Uri.parse(POST_BEAN.getUrls_posts()));
        }
        previewTimeBar.setPreviewLoader(exoPlayerManager);
        videoSurfaceView.showController();
        videoSurfaceView.setControllerShowTimeoutMs(3000);
        videoSurfaceView.setControllerVisibilityListener(visibility -> {
            if(visibility ==0){
                hide_controls();
            }else{
                show_controls();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        exoPlayerManager.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        exoPlayerManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayerManager.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
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
    }





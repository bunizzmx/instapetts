package com.bunizz.instapetts.fragments.vertical_videos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PlayVideos;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.tips.FragmentTipsViewpager;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.utils.AnalogTv.AnalogTvNoise;
import com.bunizz.instapetts.utils.HistoryView.StoryPlayerProgressView;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.LikeHeart.SmallBangView;
import com.bunizz.instapetts.utils.hearts.HeartView;
import com.bunizz.instapetts.utils.hearts.PeriscopeLayout;
import com.bunizz.instapetts.utils.line_progressbar.LineProgress;
import com.bunizz.instapetts.utils.video_player.PreviewLoader;
import com.bunizz.instapetts.utils.video_player.PreviewTumbh.ExoPlayerMediaSourceBuilder;
import com.bunizz.instapetts.utils.video_player.PreviewTumbh.GlideThumbnailTransformation;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

import org.parceler.Parcels;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class playVideoItem extends Fragment implements View.OnClickListener {

   public static  Bundle arguments;

    private ExoPlayerMediaSourceBuilder mediaSourceBuilder;
    private SimpleExoPlayer player;
    private String videoUrl;
    private  String thumbhnail;
    private ImageView imageView;
    PlayVideos postBean;

    @BindView(R.id.exoplayer_play_video)
    PlayerView videoSurfaceView;

    @BindView(R.id.preview_video)
    ImageView preview_video;

    @BindView(R.id.noise)
    AnalogTvNoise tv_noise;

    @BindView(R.id.periscope)
    PeriscopeLayout periscope;

    @BindView(R.id.title_player)
    TextView title_player;

    @BindView(R.id.descripcion_video)
    TextView descripcion_video;

    @BindView(R.id.image_video_tumbh)
    ImagenCircular image_video_tumbh;

    @BindView(R.id.progresbar_buferring)
    ProgressBar progresbar_buferring;

    @BindView(R.id.like_video)
    RelativeLayout like_video;

    @BindView(R.id.like_heart)
    SmallBangView like_heart;

    @BindView(R.id.image_heart)
    ImageView image_heart;



    @SuppressLint("MissingPermission")
    @OnClick(R.id.open_comments_video)
    void open_comments_video() {
        Bundle b = new Bundle();
        listener.open_sheetFragment(b,FragmentElement.INSTANCE_COMENTARIOS);
    }

    change_instance listener;

    public static playVideoItem newInstancex(PlayVideos postBean) {
        arguments = new Bundle();
        arguments.putParcelable("POSTS", Parcels.wrap(postBean));
        return new playVideoItem();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_play_viewpager_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(arguments!=null) {
            if (arguments.getParcelable("POSTS") != null)
                postBean = Parcels.unwrap(arguments.getParcelable("POSTS"));
        }
        Log.e("play_videos_url","-->" +  postBean.getUrl_video());
        videoUrl = postBean.getUrl_video();
        thumbhnail = postBean.getUrl_tumbh();
        this.mediaSourceBuilder = new ExoPlayerMediaSourceBuilder(videoSurfaceView.getContext());
        like_video.setOnClickListener(this);
        title_player.setText(postBean.getTitulo());
        descripcion_video.setText(postBean.getDescripcion());
        Glide.with(getContext()).load(R.drawable.logoapp).into(image_video_tumbh);
        loadThumbhnail();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void play(Uri uri) {
        mediaSourceBuilder.setUri(uri);
    }

    private void createPlayers() {
        play(Uri.parse(videoUrl));
        if (player != null) {
            player.release();
        }
        player = createFullPlayer();

        videoSurfaceView.setPlayer(player);
        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
            }
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups,
                                        TrackSelectionArray trackSelections) {
            }
            @Override
            public void onLoadingChanged(boolean isLoading) {
            }
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        Log.e("ESTATUS_VIDEO","STATE_BUFFERING");
                        progresbar_buferring.setVisibility(View.VISIBLE);
                        break;
                    case Player.STATE_ENDED:
                        Log.e("ESTATUS_VIDEO","STATE_ENDED");
                        // Log.d(TAG, "onPlayerStateChanged: Video ended.");
                        player.seekTo(0);
                        break;
                    case Player.STATE_IDLE:
                        break;
                    case Player.STATE_READY:
                        Log.e("ESTATUS_VIDEO","STATE_READY");
                        progresbar_buferring.setVisibility(View.GONE);
                        tv_noise.setVisibility(View.GONE);
                        break;
                    default:
                        Log.e("ESTATUS_VIDEO","default");
                        break;
                }
            }
            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }
            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }
            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }
            @Override
            public void onPositionDiscontinuity(int reason) {
            }
            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }
            @Override
            public void onSeekProcessed() {
            }
        });

    }

    private SimpleExoPlayer createFullPlayer() {
        TrackSelection.Factory videoTrackSelectionFactory
                = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(
                getActivity(),
                trackSelector, loadControl);
        player.setPlayWhenReady(true);
        player.prepare(mediaSourceBuilder.getMediaSource(false));
        // player.addListener(eventListener);
        return player;
    }
    public void stopPreview(long position) {
        player.seekTo(position);
        player.setPlayWhenReady(true);
    }

    private void releasePlayers() {
        if (player != null) {
            player.release();
            player = null;
        }
    }


    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            createPlayers();
        }
    }

    public void onResume() {
        super.onResume();
        Log.e("ESTATUS_ACTIVITY","ONRESUME");
        createPlayers();
    }

    public void onPause() {
        super.onPause();
        Log.e("ESTATUS_ACTIVITY","ONPAUSE");
        releasePlayers();
    }

    public void onStop() {
        super.onStop();
        Log.e("ESTATUS_ACTIVITY","onStop");
        if (Util.SDK_INT > 23) {
            releasePlayers();
        }
    }



    @Override
    public void onClick(View v) {
        if (like_heart.isSelected()) {
            like_heart.setSelected(false);
        } else {
            for(int i=0;i<5;i++)
                periscope.addHeart();
            like_heart.setSelected(true);
            like_heart.likeAnimation(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (change_instance) context;
    }

    public void loadThumbhnail(){
        Log.e("LKDLASJKDL","KAJDKA");
        Glide.with(getContext())
                .load(thumbhnail)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(preview_video);
    }
}

package com.bunizz.instapetts.fragments.vertical_videos;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PlayVideos;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.tips.FragmentTipsViewpager;
import com.bunizz.instapetts.utils.AnalogTv.AnalogTvNoise;
import com.bunizz.instapetts.utils.HistoryView.StoryPlayerProgressView;
import com.bunizz.instapetts.utils.ImagenCircular;
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

public class playVideoItem extends Fragment implements PreviewLoader,View.OnClickListener {

   public static  Bundle arguments;

    private ExoPlayerMediaSourceBuilder mediaSourceBuilder;
    private SimpleExoPlayer player;
    private String thumbnailsUrl;
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
        thumbnailsUrl = postBean.getUrl_video();
        this.mediaSourceBuilder = new ExoPlayerMediaSourceBuilder(videoSurfaceView.getContext());
        periscope.setOnClickListener(this);
        title_player.setText(postBean.getTitulo());
        descripcion_video.setText(postBean.getDescripcion());
        Glide.with(getContext()).load(R.drawable.logoapp).into(image_video_tumbh);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void play(Uri uri) {
        mediaSourceBuilder.setUri(uri);
    }

    private void createPlayers() {
        play(Uri.parse(thumbnailsUrl));
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
        if (Util.SDK_INT <= 23) {
            createPlayers();
        }
    }

    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayers();
        }
    }

    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayers();
        }
    }

    @Override
    public void loadPreview(long currentPosition, long max) {
        Log.e("LKDLASJKDL","KAJDKA");
        player.setPlayWhenReady(true);
        Glide.with(getContext())
                .load(thumbnailsUrl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .transform(new GlideThumbnailTransformation(currentPosition))
                .into(preview_video);
    }


    @Override
    public void onClick(View v) {
        periscope.addHeart();
    }
}
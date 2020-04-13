package com.bunizz.instapetts.utils.dilogs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.listeners.chose_pet_listener;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.video_player.ExoPlayerRecyclerView;
import com.bunizz.instapetts.utils.video_player.PlayerViewHolder;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class DialogPreviewPostVideo extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = true;
    private boolean isLocked = false;
    Context context;
    chose_pet_listener listener;
    PostBean postBean;
    TextView addres_post_dialog;
    ImagenCircular image_pet_dialog;
    TextView num_likes_posts_dialog,name_pet_post_dialog;
    private static final String AppName = "Android ExoPlayer";

    private ImageView mediaCoverImage, volumeControl;
    private CardView card_view_mute;
    private FrameLayout mediaContainer;
    private PlayerView videoSurfaceView;
    private SimpleExoPlayer videoPlayer;
    private VolumeState volumeState;
    private boolean isVideoViewAdded;
    private RequestManager requestManager;

    public chose_pet_listener getListener() {
        return listener;
    }

    public void setListener(chose_pet_listener listener) {
        this.listener = listener;
    }

    public DialogPreviewPostVideo(Context context, PostBean postBean,RequestManager requestManager){
        this.context = context;
        this.postBean =postBean;
        this.requestManager =requestManager;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_preview_post_video, null);
        image_pet_dialog = dialogView.findViewById(R.id.image_pet_dialog);
        addres_post_dialog = dialogView.findViewById(R.id.addres_post_dialog);
        name_pet_post_dialog = dialogView.findViewById(R.id.name_pet_post_dialog);
        mediaCoverImage= dialogView.findViewById(R.id.ivMediaCoverImage);
        num_likes_posts_dialog = dialogView.findViewById(R.id.num_likes_posts_dialog);
        card_view_mute = dialogView.findViewById(R.id.card_view_mute);
        volumeControl = dialogView.findViewById(R.id.ivVolumeControl);
        mediaContainer = dialogView.findViewById(R.id.mediaContainerPreview);
        name_pet_post_dialog.setText(this.postBean.getName_pet());
        if(this.postBean.getLikes() == 0)
           num_likes_posts_dialog.setText("se el primero en darle like");
        else
            num_likes_posts_dialog.setText("a " + this.postBean.getLikes() + " personas les gusta esto");
        Glide.with(this.context).load(this.postBean.getUrl_photo_pet()).into(image_pet_dialog);
        if(postBean.getAddress()!=null) {
            if (!postBean.getAddress().equals("INVALID")) {
                addres_post_dialog.setVisibility(VISIBLE);
                addres_post_dialog.setText(postBean.getAddress());
            } else
                addres_post_dialog.setVisibility(GONE);
        }else{
            addres_post_dialog.setVisibility(GONE);
        }
        init_video();
        playVideo();
        dialogBuilder.setView(dialogView);

        dialog = dialogBuilder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.e("DISMISSS","EXECUTE");
                onPausePlayer();
                releasePlayer();
            }
        });
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (allowAnimation) dialog.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;
    }
    public boolean is_multiple(String uris_not_parsed) {
        String parsed[]  = uris_not_parsed.split(",");
        if(parsed.length > 1)
            return  true;
        else
            return false;
    }

    void init_video(){
        videoSurfaceView = new PlayerView(this.context);
        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        //Create the player using ExoPlayerFactory
        videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        // Disable Player Control
        videoSurfaceView.setUseController(false);
        // Bind the player to the view.
        videoSurfaceView.setPlayer(videoPlayer);
        // Turn on Volume
        setVolumeControl(VolumeState.ON);
        videoPlayer.addListener(new Player.EventListener() {
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
                        Log.e(TAG, "onPlayerStateChanged: Buffering video.");
                       /* if (progressBar != null) {
                            progressBar.setVisibility(VISIBLE);
                        }*/
                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "onPlayerStateChanged: Video ended.");
                        videoPlayer.seekTo(0);
                        break;
                    case Player.STATE_IDLE:
                        break;
                    case Player.STATE_READY:
                        Log.e(TAG, "onPlayerStateChanged: Ready to play.");
                       /* if (progressBar != null) {
                            progressBar.setVisibility(GONE);
                        }*/
                        if (!isVideoViewAdded) {
                            addVideoView();
                        }
                        break;
                    default:
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


    public void playVideo() {
        if (videoSurfaceView == null) {
            return;
        }
            videoSurfaceView.setVisibility(INVISIBLE);
            videoSurfaceView.setPlayer(videoPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    context, Util.getUserAgent(context, AppName));
            String mediaUrl =  this.postBean.getUrls_posts();
            if (mediaUrl != null) {
                HlsMediaSource hlsMediaSource =
                        new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mediaUrl));

                videoPlayer.prepare(hlsMediaSource);
                videoPlayer.setPlayWhenReady(true);
            }
    }

    private void addVideoView() {
        mediaContainer.addView(videoSurfaceView);
        isVideoViewAdded = true;
        videoSurfaceView.requestFocus();
        videoSurfaceView.setVisibility(VISIBLE);
        videoSurfaceView.setAlpha(1);
        mediaCoverImage.setVisibility(GONE);
    }

    public void releasePlayer() {
        if (videoPlayer != null) {
            videoPlayer.release();
            videoPlayer = null;
        }
    }
    public void onPausePlayer() {
        if (videoPlayer != null) {
            videoPlayer.stop();
        }
    }


    @Override
    protected void finalize() throws Throwable {
        Log.d(TAG, "FINALIZEEEEEEEEEEEE");
        super.finalize();
    }





    private void toggleVolume() {
        if (videoPlayer != null) {
            if (volumeState == VolumeState.OFF) {
                Log.d(TAG, "togglePlaybackState: enabling volume.");
                setVolumeControl(VolumeState.ON);
            } else if (volumeState ==VolumeState.ON) {
                Log.d(TAG, "togglePlaybackState: disabling volume.");
                setVolumeControl(VolumeState.OFF);
            }
        }
    }
    private void setVolumeControl(VolumeState state) {
        volumeState = state;
        if (state == VolumeState.OFF) {
            videoPlayer.setVolume(0f);
            animateVolumeControl();
        } else if (state == VolumeState.ON) {
            videoPlayer.setVolume(1f);
            animateVolumeControl();
        }
    }
    private void animateVolumeControl() {
        if (volumeControl != null) {
            volumeControl.bringToFront();
            if (volumeState == VolumeState.OFF) {
                requestManager.load(R.drawable.ic_mute)
                        .into(volumeControl);
            } else if (volumeState == VolumeState.ON) {
                requestManager.load(R.drawable.ic_dismute)
                        .into(volumeControl);
            }
            volumeControl.animate().cancel();
            volumeControl.setAlpha(1f);
            volumeControl.animate()
                    .alpha(0f)
                    .setDuration(600).setStartDelay(2000);
            card_view_mute.animate().cancel();
            card_view_mute.setAlpha(1f);
            card_view_mute.animate()
                    .alpha(0f)
                    .setDuration(600).setStartDelay(2000);

        }
    }

    @Override
    public void show(){
        dialog.setCancelable(this.cancelable);
        dialog.show();
    }

    /**
     * Dismiss the dialog.
     */
    @Override
    public void dismiss(){
        onPausePlayer();
        releasePlayer();
        if(!isLocked){
            dialog.dismiss();
        }
    }

    public void setTitleVisable(boolean show){
        try{

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public Context getContext() {
        return context;
    }


    @Override
    public View getDialogView() {
        return dialogView;
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }
    @Override
    public AlertDialog getDialog() {
        return dialog;
    }

    private enum VolumeState {
        ON, OFF
    }



}

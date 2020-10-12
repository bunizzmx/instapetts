package com.bunizz.instapetts.fragments.vertical_videos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.TimeZoneFormat;
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
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.reports.ReportActiviy;
import com.bunizz.instapetts.beans.PlayVideos;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.tips.FragmentTipsViewpager;
import com.bunizz.instapetts.listeners.actions_dialog_profile;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.conexion_listener;
import com.bunizz.instapetts.listeners.isMyFragmentVisibleListener;
import com.bunizz.instapetts.listeners.simpleLikeListener;
import com.bunizz.instapetts.utils.AnalogTv.AnalogTvNoise;
import com.bunizz.instapetts.utils.HistoryView.StoryPlayerProgressView;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.LikeHeart.SmallBangView;
import com.bunizz.instapetts.utils.dilogs.DialogOptionsPosts;
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

import java.text.NumberFormat;
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

    @BindView(R.id.num_likes_video)
    TextView num_likes_video;

    @BindView(R.id.num_comentarios_video)
    TextView num_comentarios_video;

    @BindView(R.id.num_views_video)
    TextView num_views_video;

    @BindView(R.id.name_foto_user_video)
    LinearLayout name_foto_user_video;

    boolean FRAGMENT_HIDE =false;



    @SuppressLint("MissingPermission")
    @OnClick(R.id.open_comments_video)
    void open_comments_video() {
        Bundle b = new Bundle();
        b.putInt(BUNDLES.TYPE_RESOURCE_TO_COMMNET,0);
        b.putBoolean(BUNDLES.CAN_COMMENT,true);
        b.putInt(BUNDLES.ID_POST,postBean.getId());
        b.putInt(BUNDLES.ID_USUARIO,-999);
        listener.open_sheetFragment(b,FragmentElement.INSTANCE_COMENTARIOS);
    }
    @SuppressLint("MissingPermission")
    @OnClick(R.id.open_options_video)
    void open_options_video() {
        if(!App.read(PREFERENCES.MODO_INVITADO,false)) {
            DialogOptionsPosts optionsPosts = new DialogOptionsPosts(getContext(), -999, -999, "xxxx");
            optionsPosts.setListener(new actions_dialog_profile() {
                @Override
                public void delete_post(int id_post) {
                }

                @Override
                public void reportPost(int id_post) {
                    Intent reportIntent = new Intent(getActivity(), ReportActiviy.class);
                    reportIntent.putExtra("ID_RECURSO", id_post);
                    reportIntent.putExtra("TYPO_RECURSO", 1);
                    startActivity(reportIntent);
                }

                @Override
                public void unfollowUser(int id_user, String uuid) {
                }
            });
            optionsPosts.show();
        }else{
            if(conexion_list!=null){
                conexion_list.message(getContext().getString(R.string.no_action_invitado));
            }
        }
    }


    simpleLikeListener listener_video;
    change_instance listener;
    isMyFragmentVisibleListener fragmentVisible ;
    conexion_listener conexion_list ;

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
        Log.e("play_videos_url","-->" +  postBean.getUrl_tumbh());
        videoUrl = postBean.getUrl_video();
        name_foto_user_video.setVisibility(View.GONE);
        thumbhnail = postBean.getUrl_tumbh();
        this.mediaSourceBuilder = new ExoPlayerMediaSourceBuilder(videoSurfaceView.getContext());
        like_video.setOnClickListener(this);
        title_player.setText(postBean.getTitulo());
        if(postBean.getDescripcion().length() > 80){
            descripcion_video.setText(postBean.getDescripcion().substring(0,79) + "...");
        }else {
            descripcion_video.setText(postBean.getDescripcion());
        }
        num_likes_video.setText(NumberFormat.getInstance().format(postBean.getLikes()).toString());
        num_comentarios_video.setText(NumberFormat.getInstance().format(postBean.getComentarios()).toString() );
        num_views_video.setText( NumberFormat.getInstance().format(postBean.getVisto()).toString());
        Glide.with(getContext()).load(R.drawable.logoapp).into(image_video_tumbh);
        loadThumbhnail();
        if(postBean.isLiked()){
            like_heart.setSelected(true);
        }else{
            like_heart.setSelected(false);
        }
         if(listener_video!=null)
            listener_video.onLikeOrView(postBean.getId(),1);

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
        if(postBean.getAspecto().equals("16_9"))
            videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        else
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
                        preview_video.setVisibility(View.GONE);
                        progresbar_buferring.setVisibility(View.GONE);
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

    private void pausePlayer(){
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }
    private void startPlayer(){
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }


    public void onStart() {
        super.onStart();
        if(fragmentVisible.isVisible(FragmentElement.INSTANCE_PLAY_VIDEOS))
            createPlayers();

    }

    public void onResume() {
        super.onResume();
        Log.e("ESTATUS_ACTIVITY","ONRESUME");
        if(fragmentVisible.isVisible(FragmentElement.INSTANCE_PLAY_VIDEOS)) {
            if(player == null) {
                createPlayers();
            }
            progresbar_buferring.setVisibility(View.VISIBLE);
        }
    }

    public void onPause() {
        super.onPause();
        Log.e("ESTATUS_ACTIVITY","ONPAUSE");
        preview_video.setVisibility(View.VISIBLE);
        releasePlayers();
    }

    public void onStop() {
        super.onStop();
        Log.e("ESTATUS_ACTIVITY","onStop");
        preview_video.setVisibility(View.VISIBLE);
        releasePlayers();

    }



    @Override
    public void onClick(View v) {

            listener_video.onLikeOrView(postBean.getId(),0);

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
        listener_video =(simpleLikeListener)context;
        fragmentVisible = (isMyFragmentVisibleListener)context;
       conexion_list = ( conexion_listener )context;
    }

    public void loadThumbhnail(){

        Glide.with(getContext())
                .load(thumbhnail)
                .into(preview_video);
    }

    public void stopPlayers(){
        Log.e("ESTATUS_ACTIVITY","stopPlayers");
        pausePlayer();
       // releasePlayers();
    }

    public void reanudarPLayers(){

        Log.e("ESTATUS_ACTIVITY","release players");
        if(preview_video!=null) {
            progresbar_buferring.setVisibility(View.VISIBLE);
            preview_video.setVisibility(View.VISIBLE);
        }
        startPlayer();
       // createPlayers();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("ESTATUS_ACTIVITY","onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("ESTATUS_ACTIVITY","onDestroyView");
    }
}

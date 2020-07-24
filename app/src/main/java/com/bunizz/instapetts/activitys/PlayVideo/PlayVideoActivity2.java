package com.bunizz.instapetts.activitys.PlayVideo;

import android.Manifest;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.TipsBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.db.Utilities;
import com.bunizz.instapetts.utils.dilogs.DialogProgresCrop;
import com.bunizz.instapetts.utils.trimVideoView.VideoTrimmerView;
import com.bunizz.instapetts.utils.videoCrop.cropview.window.CropVideoView;
import com.bunizz.instapetts.utils.videoCrop.ffmpeg.ExecuteBinaryResponseHandler;
import com.bunizz.instapetts.utils.videoCrop.ffmpeg.FFmpeg;
import com.bunizz.instapetts.utils.videoCrop.ffmpeg.FFtask;
import com.bunizz.instapetts.utils.videoCrop.player.VideoPlayer;
import com.google.android.exoplayer2.util.Util;

import org.parceler.Parcels;

import java.io.File;
import java.util.Formatter;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayVideoActivity2 extends AppCompatActivity implements VideoPlayer.OnProgressUpdateListener {
    private static final String VIDEO_CROP_INPUT_PATH = "VIDEO_CROP_INPUT_PATH";
    private static final String VIDEO_CROP_OUTPUT_PATH = "VIDEO_CROP_OUTPUT_PATH";
    private static final int STORAGE_REQUEST = 100;

    private VideoPlayer mVideoPlayer;
    private StringBuilder formatBuilder;
    private Formatter formatter;
    DialogProgresCrop dialogProgresCrop;

    private CropVideoView mCropVideoView;

    private String inputPath;
    private boolean isVideoPlaying = false;
    private boolean isAspectMenuShown = false;
    long STARTCROP = 0;//mTmbProgress.getLeftProgress();
    long DURATION = 0;//mTmbProgress.getRightProgress() - mTmbProgress.getLeftProgress();
    String ASPECT="";
    TipsBean TIPS_BEAN;
    PostBean POST_BEAN;
    int TYPE_PLAYER=0;

    public static Intent createIntent(Context context, String inputPath, String outputPath) {
        Intent intent = new Intent(context, PlayVideoActivity.class);
        intent.putExtra(VIDEO_CROP_INPUT_PATH, inputPath);
        intent.putExtra(VIDEO_CROP_OUTPUT_PATH, outputPath);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop2);
        ButterKnife.bind(this);
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());

        if (getIntent() != null) {
            TYPE_PLAYER = getIntent().getIntExtra("TYPE_PLAYER",0);
            if(TYPE_PLAYER == 0)
                TIPS_BEAN = Parcels.unwrap(getIntent().getParcelableExtra("BEAN"));
            else {
                if( Parcels.unwrap(getIntent().getParcelableExtra("BEAN")) instanceof PostBean){
                    Log.e("CLICK_PARSED","-->es post");
                }else{
                    Log.e("CLICK_PARSED","-->no es post");
                }
                POST_BEAN = Parcels.unwrap(getIntent().getParcelableExtra("BEAN"));
            }

        }

        inputPath = POST_BEAN.getUrls_posts();
        mVideoPlayer = new VideoPlayer(this);
        mVideoPlayer.initMediaSourceWeb(this, Uri.parse(inputPath));
        Log.e("URI_VIDEO","-->" + inputPath);
        findViews();


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initPlayer(inputPath);
                } else {
                    Toast.makeText(this, "You must grant a write storage permission to use this functionality", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isVideoPlaying) {
            mVideoPlayer.play(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mVideoPlayer.play(false);
    }

    @Override
    public void onDestroy() {
        mVideoPlayer.release();
        super.onDestroy();
    }



    private void findViews() {
        mCropVideoView = findViewById(R.id.cropVideoView);
    }


    private void playPause() {
        isVideoPlaying = !mVideoPlayer.isPlaying();
        if (mVideoPlayer.isPlaying()) {
            mVideoPlayer.play(!mVideoPlayer.isPlaying());
          //  mTmbProgress.setSliceBlocked(false);
          //  mTmbProgress.removeVideoStatusThumb();
          //  mIvPlay.setImageResource(R.drawable.ic_play);
            return;
        }
        //mVideoPlayer.seekTo(mTmbProgress.getLeftProgress());
        //mVideoPlayer.play(!mVideoPlayer.isPlaying());
       // mTmbProgress.videoPlayingProgress(mTmbProgress.getLeftProgress());
      //  mIvPlay.setImageResource(R.drawable.ic_pause);
    }

    private void initPlayer(String uri) {
        mVideoPlayer = new VideoPlayer(this);
        ASPECT ="4_5";
        mVideoPlayer.initMediaSourceWeb(this, Uri.parse(uri));
        mVideoPlayer.setUpdateListener(this);
        fetchVideoInfo(uri);
    }

    private void fetchVideoInfo(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(new File(uri).getAbsolutePath());
        int videoWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int videoHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        int rotationDegrees = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));

        mCropVideoView.initBounds(videoWidth, videoHeight, rotationDegrees);
    }

    private void handleMenuVisibility() {
        isAspectMenuShown = !isAspectMenuShown;
        TimeInterpolator interpolator;
        if (isAspectMenuShown) {
            interpolator = new DecelerateInterpolator();
        } else {
            interpolator = new AccelerateInterpolator();
        }

    }



    @SuppressLint("DefaultLocale")
    private void handleCropStart() {

        Rect cropRect = mCropVideoView.getCropRect();
        if(STARTCROP < 0){
            STARTCROP =0;
        }
        String start = Util.getStringForTime(formatBuilder, formatter, STARTCROP);
        String duration = Util.getStringForTime(formatBuilder, formatter, DURATION);
        start += "." + STARTCROP % 1000;
        duration += "." + DURATION % 1000;

        Log.e("INIDCES_VIDEO","--> : "+ STARTCROP + "/" + DURATION);
        Log.e("INIDCES_VIDEO","--> : "+ start + "/" + duration);

    }


    private void playVideo(Long startMillis,  Long endMillis) {
        STARTCROP = startMillis;
        mVideoPlayer.seekTo(startMillis);
        mVideoPlayer.play(true);
    }

    @Override
    public void onProgressUpdate(long currentPosition, long duration, long bufferedPosition) {

    }

    @Override
    public void onFirstTimeUpdate(long duration, long currentPosition) {

    }
}

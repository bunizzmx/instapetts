package com.bunizz.instapetts.utils.videoCrop;

import android.Manifest;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.db.Utilities;
import com.bunizz.instapetts.utils.dilogs.DialogProgresCrop;
import com.bunizz.instapetts.utils.snackbar.SnackBar;
import com.bunizz.instapetts.utils.trimVideoView.VideoTrimmerView;
import com.bunizz.instapetts.utils.videoCrop.cropview.window.CropVideoView;
import com.bunizz.instapetts.utils.videoCrop.ffmpeg.ExecuteBinaryResponseHandler;
import com.bunizz.instapetts.utils.videoCrop.ffmpeg.FFmpeg;
import com.bunizz.instapetts.utils.videoCrop.ffmpeg.FFtask;
import com.bunizz.instapetts.utils.videoCrop.player.VideoPlayer;
import com.bunizz.instapetts.utils.videocrop4.TrimmerUtils;
import com.google.android.exoplayer2.util.Util;

import Jni.FFmpegCmd;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Formatter;
import java.util.Locale;

import VideoHandle.OnEditorListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoCropActivity extends AppCompatActivity implements VideoPlayer.OnProgressUpdateListener,VideoTrimmerView.OnSelectedRangeChangedListener {
    private static final String VIDEO_CROP_INPUT_PATH = "VIDEO_CROP_INPUT_PATH";
    private static final String VIDEO_CROP_OUTPUT_PATH = "VIDEO_CROP_OUTPUT_PATH";
    private static final int STORAGE_REQUEST = 100;

    private VideoPlayer mVideoPlayer;
    private StringBuilder formatBuilder;
    private Formatter formatter;
    DialogProgresCrop dialogProgresCrop;

    private CropVideoView mCropVideoView;
    MediaMetadataRetriever mediaMetadataRetriever;
    private String inputPath;
    private String outputPath;
    private boolean isVideoPlaying = false;
    private boolean isAspectMenuShown = false;
    long STARTCROP = 0;
    long DURATION = 30;
    String ASPECT="";

    public static Intent createIntent(Context context, String inputPath, String outputPath) {
        Intent intent = new Intent(context, VideoCropActivity.class);
        intent.putExtra(VIDEO_CROP_INPUT_PATH, inputPath);
        intent.putExtra(VIDEO_CROP_OUTPUT_PATH, outputPath);
        return intent;
    }
    @BindView(R.id.videoTrimmerView)
    VideoTrimmerView videoTrimmerView;

    @BindView(R.id.durationView)
    TextView durationView;
    boolean IS_IN_CROP = false;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.btSave)
    void btSave() {
        if(!IS_IN_CROP){
            handleCropStart();
        }
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.back_to_main)
    void back_to_main() {
       finish();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ButterKnife.bind(this);
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        inputPath = getIntent().getStringExtra(VIDEO_CROP_INPUT_PATH);
        outputPath = getIntent().getStringExtra(VIDEO_CROP_OUTPUT_PATH);
        findViews();
        requestStoragePermission();
        mediaMetadataRetriever = new MediaMetadataRetriever();
         mediaMetadataRetriever.setDataSource(inputPath);
        int heightData = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        int widthData = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int rotation = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        Log.e("ROROROR","-->" + rotation);

        if(heightData < widthData){
            ASPECT ="16_9";
        }else
        {
            ASPECT =" 4_3 ";
        }
        switch (rotation){
            case 0:
                if(heightData < widthData)
                    ASPECT ="16_9_ROTATION_0";
                else
                    ASPECT =" 4_3_ROTATION_0";

                break;
            case 90:
                if(heightData < widthData)
                    ASPECT ="16_9_ROTATION_90";
                else
                    ASPECT =" 4_3_ROTATION_90";
                break;
            case 180:
                if(heightData < widthData)
                    ASPECT ="16_9_ROTATION_180";
                else
                    ASPECT =" 4_3_ROTATION_180";
                break;
            case 270:
                if(heightData < widthData)
                    ASPECT ="16_9_ROTATION_270";
                else
                    ASPECT =" 4_3_ROTATION_270";
                break;
        }

        new Handler().postDelayed(() ->play(), 500);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    public void play(){
        videoTrimmerView
                .setVideo(new File(inputPath))
                .setMaxDuration(40_000)
                .setMinDuration(1_000)
                .setFrameCountInWindow(8)
                .setExtraDragSpace(Utilities.convertDpToPixel(2f,VideoCropActivity.this))
                .setOnSelectedRangeChangedListener(this)
                .show();
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
        if (!new File(uri).exists()) {
            Toast.makeText(this, "File doesn't exists", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        mVideoPlayer = new VideoPlayer(this);
        mCropVideoView.setPlayer(mVideoPlayer.getPlayer());
        mVideoPlayer.initMediaSource(this, uri);
        mVideoPlayer.setUpdateListener(this);
        fetchVideoInfo(uri);
    }

    private void fetchVideoInfo(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(new File(uri).getAbsolutePath());
        int videoWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int videoHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        int rotationDegrees = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        Log.e("ROTATE_dEGRESS","--:" + rotationDegrees);
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

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
        } else {
            initPlayer(inputPath);
        }
    }

    @SuppressLint("DefaultLocale")
    private void handleCropStart() {
        if ((DURATION / 1000) >= 10){
            IS_IN_CROP = true;
        if (STARTCROP < 0) {
            STARTCROP = 0;
        }
        String start = Util.getStringForTime(formatBuilder, formatter, STARTCROP);
        String duration = Util.getStringForTime(formatBuilder, formatter, DURATION);
        start += "." + STARTCROP % 1000;
        duration += "." + DURATION % 1000;

        Log.e("INIDCES_VIDEO", "--> : " + (STARTCROP / 1000) + "/" + ((DURATION / 1000) + (STARTCROP / 1000)));
        Log.e("INIDCES_VIDEO", "--> : " + start + "/" + duration);


        String[] complexCommand =
                {"ffmpeg", "-ss", TrimmerUtils.formatCSeconds((STARTCROP / 1000)), "-i", String.valueOf(inputPath), "-to", TrimmerUtils.formatCSeconds(((DURATION / 1000) + (STARTCROP / 1000))), "-c", "copy", outputPath};
        dialogProgresCrop = new DialogProgresCrop(VideoCropActivity.this);
        dialogProgresCrop.show();
        FFmpegCmd.exec(complexCommand, 0, new OnEditorListener() {
            @Override
            public void onSuccess() {
                dialogProgresCrop.dismiss();
                IS_IN_CROP = false;
                Intent intent = new Intent();
                intent.putExtra(BUNDLES.VIDEO_DURATION, (DURATION / 1000));
                intent.putExtra(BUNDLES.VIDEO_ASPECT, ASPECT);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure() {
                IS_IN_CROP = false;
            }

            @Override
            public void onProgress(float progress) {
                dialogProgresCrop.set_progress_percentage(progress);

            }
        });
    }else{
            View v = findViewById(R.id.root_crop);
            SnackBar.info(v, "El video debe ser mayor a 10 seg.", SnackBar.LENGTH_LONG).show();
        }

    }

    @Override
    public void onSelectRangeStart() {

    }

    @Override
    public void onSelectRange(long startMillis, long endMillis) {
        showDuration(startMillis, endMillis);
    }

    @Override
    public void onSelectRangeEnd(long startMillis, long endMillis) {
        showDuration(startMillis, endMillis);
        playVideo(startMillis, endMillis);
    }

    private void showDuration(Long startMillis,Long endMillis) {
        Long duration = (endMillis - startMillis) / 1000L;
        STARTCROP = startMillis;
        DURATION = endMillis - startMillis;
        Log.e("DURACION_VIDEO","-->x :"  + (DURATION /1000));
        durationView.setText(duration + " seconds selected");
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
        Log.e("DURACION_VIDEO","-->1" + duration  + "/" + currentPosition);

    }
}

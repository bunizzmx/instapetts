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
    private FFtask mFFTask;
    private FFmpeg mFFMpeg;
    long STARTCROP = 0;//mTmbProgress.getLeftProgress();
    long DURATION = 0;//mTmbProgress.getRightProgress() - mTmbProgress.getLeftProgress();
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

   /* @SuppressLint("MissingPermission")
    @OnClick(R.id.change_16_9)
    void change_16_9() {
        ASPECT ="16_9";
        mCropVideoView.setFixedAspectRatio(true);
        mCropVideoView.setAspectRatio(16, 9);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.change_4_5)
    void change_4_5() {
        ASPECT ="1_2";
        mCropVideoView.setFixedAspectRatio(true);
        mCropVideoView.setAspectRatio(1, 2);
        App.getInstance().aspectMobile();
    }



    @SuppressLint("MissingPermission")
    @OnClick(R.id.change_4_3)
    void change_4_3() {
        ASPECT ="4_3";
        mCropVideoView.setFixedAspectRatio(true);
        mCropVideoView.setAspectRatio(4, 3);
    }*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ButterKnife.bind(this);
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());

        inputPath = getIntent().getStringExtra(VIDEO_CROP_INPUT_PATH);
        outputPath = getIntent().getStringExtra(VIDEO_CROP_OUTPUT_PATH);

        if (TextUtils.isEmpty(inputPath) || TextUtils.isEmpty(outputPath)) {
            Toast.makeText(this, "input and output paths must be valid and not null", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }
        Log.e("URI_VIDEO","-->" + inputPath);
        findViews();
        requestStoragePermission();
        mediaMetadataRetriever = new MediaMetadataRetriever();
         mediaMetadataRetriever.setDataSource(inputPath);
        int heightData = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        int widthData = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        Log.e("WIDTH_HSDHDHD","-->" + heightData  + "/" + widthData);
        if(heightData < widthData){
            ASPECT ="16_9";
        }else
        {
            ASPECT =" 4_3 ";
        }

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        videoTrimmerView
                .setVideo(new File(inputPath))
                .setFrameCountInWindow(8)
                .setExtraDragSpace(Utilities.convertDpToPixel(2f,VideoCropActivity.this))
                .setOnSelectedRangeChangedListener(this)
                .setMaxDuration(30000)
                .setMinDuration(5000)
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
        if (mFFTask != null && !mFFTask.isProcessCompleted()) {
            mFFTask.sendQuitSignal();
        }
        if (mFFMpeg != null) {
            mFFMpeg.deleteFFmpegBin();
        }
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
        mCropVideoView.setFixedAspectRatio(true);
        ASPECT ="1_2";
        mCropVideoView.setAspectRatio(1, 2);
        mCropVideoView.setPlayer(mVideoPlayer.getPlayer());
        mVideoPlayer.initMediaSource(this, uri);
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
        IS_IN_CROP = true;
        Rect cropRect = mCropVideoView.getCropRect();
        if(STARTCROP < 0){
            STARTCROP =0;
        }
        String start = Util.getStringForTime(formatBuilder, formatter, STARTCROP);
        String duration = Util.getStringForTime(formatBuilder, formatter, DURATION);
        start += "." + STARTCROP % 1000;
        duration += "." + DURATION % 1000;

        Log.e("INIDCES_VIDEO","--> : "+ (STARTCROP/1000) + "/" + ((DURATION / 1000) + (STARTCROP / 1000)));
        Log.e("INIDCES_VIDEO","--> : "+ start + "/" + duration);


        String[] complexCommand =
                {"ffmpeg","-ss",TrimmerUtils.formatCSeconds((STARTCROP/1000))
                ,"-i",String.valueOf(inputPath),"-to", TrimmerUtils.formatCSeconds(((DURATION / 1000) + (STARTCROP / 1000))),"-c","copy",outputPath};
        dialogProgresCrop = new DialogProgresCrop(VideoCropActivity.this);
        dialogProgresCrop.show();
        FFmpegCmd.exec(complexCommand, 0, new OnEditorListener() {
            @Override
            public void onSuccess() {
                dialogProgresCrop.dismiss();
                IS_IN_CROP = false;
                Intent intent = new Intent();
                intent.putExtra(BUNDLES.VIDEO_DURATION,(DURATION /1000));
                intent.putExtra(BUNDLES.VIDEO_ASPECT,ASPECT);
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onFailure() {
                IS_IN_CROP = false;
                Toast.makeText(VideoCropActivity.this, "Failed to crop!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(float progress) {
                dialogProgresCrop.set_progress_percentage(progress);

            }
        });
      /*  mFFMpeg = FFmpeg.getInstance(this);
       if (mFFMpeg.isSupported()) {
            String[] complexCommand = {"-ss", "" + start, "-y", "-i",inputPath , "-t", "" + duration,"-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050", outputPath};
           mFFTask = mFFMpeg.execute(complexCommand, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    IS_IN_CROP = false;
                    Intent intent = new Intent();
                    intent.putExtra(BUNDLES.VIDEO_DURATION,(DURATION /1000));
                    intent.putExtra(BUNDLES.VIDEO_ASPECT,ASPECT);
                    setResult(RESULT_OK,intent);
                    finish();
                }

                @Override
                public void onProgress(String message) {
                    Log.e("onProgress", message);
                }

                @Override
                public void onFailure(String message) {
                    IS_IN_CROP = false;
                    Toast.makeText(VideoCropActivity.this, "Failed to crop!", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure", message);
                }

                @Override
                public void onProgressPercent(float percent) {
                    dialogProgresCrop.set_progress_percentage(percent);
                }

                @Override
                public void onStart() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogProgresCrop = new DialogProgresCrop(VideoCropActivity.this);
                            dialogProgresCrop.show();
                        }
                    });
                }

                @Override
                public void onFinish() {
                    IS_IN_CROP = false;
                    dialogProgresCrop.dismiss();
                }
            }, DURATION * 1.0f / 1000);
        }*/
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

    }
}

package com.bunizz.instapetts.fragments.share_post.cropVideo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bunizz.instapetts.R;

import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;

import com.bunizz.instapetts.utils.compressorVideo.VideoCompressor;
import com.bunizz.instapetts.utils.cropVideo.cropview.window.CropVideoView;
import com.bunizz.instapetts.utils.cropVideo.ffmpeg.ExecuteBinaryResponseHandler;
import com.bunizz.instapetts.utils.cropVideo.ffmpeg.FFmpeg;
import com.bunizz.instapetts.utils.cropVideo.ffmpeg.FFtask;
import com.bunizz.instapetts.utils.cropVideo.player.VideoPlayer;
import com.bunizz.instapetts.utils.cropVideo.view.TimeLineView;
import com.bunizz.instapetts.utils.cropVideo.view.VideoSliceSeekBarH;
import com.bunizz.instapetts.utils.dilogs.DialogProgresCrop;
import com.bunizz.instapetts.utils.trimVideo2.K4LVideoTrimmer;
import com.bunizz.instapetts.utils.trimVideo2.interfaces.OnK4LVideoListener;
import com.bunizz.instapetts.utils.trimVideo2.interfaces.OnTrimVideoListener;
import com.daasuu.mp4compose.FillMode;
import com.daasuu.mp4compose.FillModeCustomItem;
import com.daasuu.mp4compose.Rotation;
import com.daasuu.mp4compose.composer.Mp4Composer;
import com.google.android.exoplayer2.util.Util;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG;

public class VideoCropFragment extends Fragment implements   OnTrimVideoListener, OnK4LVideoListener {


    @BindView(R.id.cropVideoView)
    K4LVideoTrimmer trimmer_view;





    private StringBuilder formatBuilder;
    private Formatter formatter;

    ProgressDialog progressDialog;
    private VideoPlayer mVideoPlayer;
    changue_fragment_parameters_listener listener;
    DialogProgresCrop dialogProgresCrop;
    private FFtask mFFTask;
    private FFmpeg mFFMpeg;
    String path = "";
    public static VideoCropFragment newInstance() {
        return new VideoCropFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crop_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        int maxDuration = 60;
        Bundle bundle=getArguments();
        if(bundle!=null){
            path = bundle.getString("PATH_TO_CROP");
            maxDuration = bundle.getInt("DURATION");
        }
       if (trimmer_view != null) {
            trimmer_view.setMaxDuration(30);
            trimmer_view.setOnTrimVideoListener(this);
            trimmer_view.setOnK4LVideoListener(this);
            trimmer_view.setVideoURI(Uri.parse(path));
            trimmer_view.setVideoInformationVisibility(true);
        }

    }



    @Override public void onPause() {
        super.onPause();
    }

    @Override public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }


 /*   @Override
    public void onTrimStarted() {

    }

    @Override
    public void getResult(final Uri uri) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogProgresCrop = new DialogProgresCrop(getActivity());
                dialogProgresCrop.show();
            }
        });

        corp_video(uri.getPath());
    }

    @Override
    public void cancelAction() {
    }

    @Override
    public void onError(final String message) { }

    @Override
    public void onVideoPrepared() {}*/


    void corp_video(String path){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogProgresCrop = new DialogProgresCrop(getActivity());
                dialogProgresCrop.show();
            }
        });
        Log.e("COMPRESS_VIDEO","---->");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "Instapetts");
        if (!file.exists()) {
            file.mkdir();
        }
        String destino= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "Instapetts/"+"INSTAPETS_" + UUID.randomUUID() + ".mp4";
        Log.e("COMPRESS_VIDEO","---->"+destino);
        new Mp4Composer(path, destino)
                .fillMode(FillMode.PRESERVE_ASPECT_CROP)
                .listener(new Mp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        if(progress *100 > 95){
                            dialogProgresCrop.dismiss();
                        }
                        dialogProgresCrop.set_progress_percentage(progress *100);
                    }

                    @Override
                    public void onCompleted() {
                        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri fileContentUri = Uri.parse(destino);
                        mediaScannerIntent.setData(fileContentUri);
                        getActivity().sendBroadcast(mediaScannerIntent);
                        Bundle b = new Bundle();
                        ArrayList<String> uri_videos = new ArrayList<>();
                        uri_videos.add(destino);
                        b.putStringArrayList("data_pahs",uri_videos);
                        b.putInt("is_video",1);
                        listener.change_fragment_parameter(FragmentElement.INSTANCE_SHARE,b);
                    }

                    @Override
                    public void onCanceled() {
                        Log.d(TAG, "onCanceled");
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Log.e(TAG, "onFailed()", exception);
                    }
                })
                .start();
    }


    private Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }


    @Override
    public void onVideoPrepared() {

    }

    @Override
    public void onTrimStarted() {

    }

    @Override
    public void getResult(Uri uri) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogProgresCrop = new DialogProgresCrop(getActivity());
                dialogProgresCrop.show();
            }
        });

        corp_video(uri.getPath());
    }

    @Override
    public void cancelAction() {

    }

    @Override
    public void onError(String message) {

    }
}

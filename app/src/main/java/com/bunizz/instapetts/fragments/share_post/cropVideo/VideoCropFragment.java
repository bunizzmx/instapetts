package com.bunizz.instapetts.fragments.share_post.cropVideo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.bunizz.instapetts.utils.dilogs.DialogProgresCrop;
import com.bunizz.instapetts.utils.trimVideo.interfaces.VideoTrimListener;
import com.bunizz.instapetts.utils.trimVideo.utils.ToastUtil;

import com.bunizz.instapetts.utils.trimVideo.widget.VideoTrimmerView;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoCropFragment extends Fragment {

    @BindView(R.id.layout_surface_view)
    RelativeLayout mLinearVideo;

    @BindView(R.id.trimmer_view)
    VideoTrimmerView trimmer_view;
    ProgressDialog progressDialog;
    changue_fragment_parameters_listener listener;
    DialogProgresCrop dialogProgresCrop;
    public static VideoCropFragment newInstance() {
        return new VideoCropFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String path = "";
        int maxDuration = 60;
        Bundle bundle=getArguments();
        if(bundle!=null){
            path = bundle.getString("PATH_TO_CROP");
            maxDuration = bundle.getInt("DURATION");
        }
        trimmer_view.initVideoByURI(Uri.parse(path));
        trimmer_view.setOnTrimVideoListener(new VideoTrimListener() {
            @Override
            public void onStartTrim() {
                dialogProgresCrop = new DialogProgresCrop(getActivity());
                dialogProgresCrop.show();

            }

            @Override
            public void onFinishTrim(String url) {
                Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri fileContentUri = Uri.parse(url);
                mediaScannerIntent.setData(fileContentUri);
                getActivity().sendBroadcast(mediaScannerIntent);
                Bundle b = new Bundle();
                ArrayList<String> uri_videos = new ArrayList<>();
                uri_videos.add(url);
                b.putStringArrayList("data_pahs",uri_videos);
                b.putInt("is_video",0);
                trimmer_view.onDestroy();
                listener.change_fragment_parameter(FragmentElement.INSTANCE_SHARE,b);
                dialogProgresCrop.dismiss();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(double progress) {
                Log.e("XXXX","--<" + progress * 100);
                  dialogProgresCrop.set_progress_percentage( progress *100);
            }
        });


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
}

package com.bunizz.instapetts.fragments.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.feed.FeedPresenter;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.utils.crop.CropLayout;
import com.bunizz.instapetts.utils.crop.OnCropListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraPreviewStoryFragment extends Fragment {


    @BindView(R.id.crop_view)
    CropLayout crop_view;

    @BindView(R.id.publish_stories)
    CardView publish_stories;

    @BindView(R.id.crop_cuadrada)
    CardView crop_cuadrada;

    @BindView(R.id.crop_3_4)
    CardView crop_3_4;

    @BindView(R.id.crop_9_16)
    CardView crop_9_16;


    changue_fragment_parameters_listener listener;
    FeedAdapter feedAdapter;

    uploads listenr_uploads;

    ArrayList<Object> data = new ArrayList<>();

    public static CameraPreviewStoryFragment newInstance() {
        return new CameraPreviewStoryFragment();
    }
    ArrayList<String> path=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            path = bundle.getStringArrayList("PATH_SELECTED");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera_preview_story, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        crop_view.setUri(Uri.parse(path.get(0)));
        crop_view.addOnCropListener(new OnCropListener() {
            @Override
            public void onSuccess(@NotNull Bitmap bitmap) {
                if(saveImage(bitmap,"Instapetts","Instapetts_", Bitmap.CompressFormat.JPEG)){
                }
            }

            @Override
            public void onFailure(@NotNull Exception e) {

            }
        });

        publish_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               crop_view.crop();
            }
        });
        crop_cuadrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crop_view.modify_cropper(0.5f);
            }
        });

        crop_3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crop_view.modify_cropper(0.7f);
            }
        });
        crop_9_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crop_view.modify_cropper(1f);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
        listenr_uploads = (uploads)context;
    }


    public  boolean saveImage(Bitmap bitmap, String folderName, String filename, Bitmap.CompressFormat compressFormat) {
        String PATH_TEMP ="";
        filename = filename + UUID.randomUUID();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + folderName);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + folderName + File.separator + filename + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(compressFormat, 90, out);
            out.flush();
            out.close();
            PATH_TEMP = file.getPath();
            Log.e("PATH_CROPED","-->" + PATH_TEMP);
            try {
                Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri fileContentUri = Uri.fromFile(file);
                mediaScannerIntent.setData(fileContentUri);
                getContext().sendBroadcast(mediaScannerIntent);
                listenr_uploads.setResultForOtherChanges(PATH_TEMP.replace("file:",""));
            }catch (Exception e){
                Log.e("ERROR_BROADCAST",":)");
            }
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            Log.e("ImageViewZoom", exception.getMessage());
            return true;

        }
    }

}


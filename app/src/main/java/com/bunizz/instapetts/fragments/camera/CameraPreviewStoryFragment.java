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
import android.widget.LinearLayout;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.feed.adapters.FeedAdapter;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.chose_pet_listener;
import com.bunizz.instapetts.listeners.hisotry_listener;
import com.bunizz.instapetts.utils.crop2.view.ImageCropView;
import com.bunizz.instapetts.utils.dilogs.DialogShosePet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraPreviewStoryFragment extends Fragment {


    @BindView(R.id.crop_view)
    ImageCropView crop_view;

    @BindView(R.id.publish_stories)
    CardView publish_stories;

    @BindView(R.id.crop_cuadrada)
    LinearLayout crop_cuadrada;

    @BindView(R.id.crop_3_4)
    LinearLayout crop_3_4;

    @BindView(R.id.crop_9_16)
    LinearLayout crop_9_16;

    String URL_FOTO_PET;
    String NAME_PET;
    int ID_PET;
    PetHelper helper;
    ArrayList<PetBean> pets_cuerrent = new ArrayList<>();
    changue_fragment_parameters_listener listener;
    FeedAdapter feedAdapter;

    hisotry_listener listenr_uploads;

    ArrayList<Object> data = new ArrayList<>();

    @OnClick(R.id.close_camera)
    void close_camera()
    {
        getActivity().onBackPressed();
    }



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
        helper = new PetHelper(getContext());
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
        pets_cuerrent = helper.getMyPets();
        publish_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogShosePet dialogShosePet = new DialogShosePet(getActivity());
                dialogShosePet.setPetBeans(pets_cuerrent,true);
                dialogShosePet.setListener(new chose_pet_listener() {
                    @Override
                    public void chose(String url_foto, int id_pet, String name_pet, int type_pet, String name_raza) {
                        URL_FOTO_PET = url_foto;
                        NAME_PET = name_pet;
                        ID_PET=id_pet;
                        if(saveImage(crop_view.getCroppedImage(),"Instapetts","Instapetts_", Bitmap.CompressFormat.JPEG)){
                        }
                    }

                    @Override
                    public void request_no_pets() {

                    }
                });
                dialogShosePet.show();

            }
        });
        crop_cuadrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crop_view.setAspectRatio(4,4);
            }
        });

        crop_3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crop_view.setAspectRatio(3,4);
            }
        });
        crop_9_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crop_view.setAspectRatio(9,16);
            }
        });
        crop_view.setImageFilePath(path.get(0));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
        listenr_uploads = (hisotry_listener)context;
    }


    public  boolean saveImage(Bitmap bitmap, String folderName, String filename, Bitmap.CompressFormat compressFormat) {
        String PATH_TEMP ="";
        try {
            PATH_TEMP = App.getInstance().saveImage(bitmap,folderName,filename,compressFormat,0);
            Log.e("PATH_CROPED","-->" + PATH_TEMP);
            try {
                Bundle b = new Bundle();
                b.putInt(BUNDLES.ID_PET,ID_PET);
                b.putString(BUNDLES.PATH_SELECTED,PATH_TEMP.replace("file:",""));
                b.putString(BUNDLES.NAME_PET,NAME_PET);
                b.putString(BUNDLES.URL_PHOTO_PET,URL_FOTO_PET);
                listenr_uploads.chose_complete(b);
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

    public void refresh_path(){
        if(publish_stories!=null && crop_view!=null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                path = bundle.getStringArrayList("PATH_SELECTED");
            }
            crop_view.setImageFilePath(path.get(0));
        }

    }


}


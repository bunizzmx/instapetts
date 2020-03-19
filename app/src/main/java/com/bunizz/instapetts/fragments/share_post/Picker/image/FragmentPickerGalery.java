package com.bunizz.instapetts.fragments.share_post.Picker.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.utils.crop.CropLayout;
import com.bunizz.instapetts.utils.crop.OnCropListener;
import com.bunizz.instapetts.utils.imagePicker.data.Album;
import com.bunizz.instapetts.utils.imagePicker.data.Config;
import com.bunizz.instapetts.utils.imagePicker.data.Image;
import com.bunizz.instapetts.utils.imagePicker.helper.EmptySupportedRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPickerGalery  extends Fragment implements  FeedContract.View, ImagePickerContract.View,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        ImageListRecyclerViewAdapter.OnItemClickListener,
        ImageListRecyclerViewAdapter.OnItemLongClickListener{

    String PATH_TEMP="-";

    @BindView(R.id.crop_now)
    Button crop_now;

    @BindView(R.id.spinner_album)
    AppCompatSpinner spinner_album;
    final Handler handler = new Handler();


    @BindView(R.id.recycler_view)
    EmptySupportedRecyclerView recyclerView;

    @BindView(R.id.progres_chargin_photos)
    ProgressBar progres_chargin_photos;


    ImageListRecyclerViewAdapter adapter;
    ImagePickerPresenter presenter;
    ArrayAdapter albumAdapter;
    Config config;
    uploads uploas_listener;

    ArrayList<String> paths = new ArrayList<>();

    changue_fragment_parameters_listener listener;

  //  private enum class Tag { SPINNER_ALBUM, IMAGE, BUTTON_SETTING };

    public static FragmentPickerGalery newInstance() {
        return new FragmentPickerGalery();
    }
   int is_from_profile =0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ImageListRecyclerViewAdapter(getContext());
        config = new Config();
        config.setPackageName(getContext().getPackageName());
        albumAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);
        presenter = new ImagePickerPresenter(this,getContext(),config);
        paths.clear();
        Bundle bundle=getArguments();
        if(bundle!=null){
            is_from_profile = bundle.getInt("FROM_PROFILE");
            Log.e("FROM_PROFILE","--->" + is_from_profile);
            if(is_from_profile == 1){
                config.setMaxCount(1);
            }else{
                config.setMaxCount(5);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_imagepicker_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        //recyclerView.setTag(ImagePickerFragment.IMAGE);
        recyclerView.setAdapter(adapter);
        crop_now.setOnClickListener(view1 -> {
            Bundle b = new Bundle();
            ArrayList<String> uri = new ArrayList<>();
            List<Image> images_parameter = new ArrayList<>();
            images_parameter = adapter.getSelectedImages();
            for (int i =0; i<images_parameter.size();i++){
                uri.add(images_parameter.get(i).getPath());
            }
            b.putStringArrayList("PATH_SELECTED",uri);
            if(listener!=null){
                listener.change_fragment_parameter(FragmentElement.INSTANCE_CROP_IMAGE,b);
            }
        });
        spinner_album.setAdapter(albumAdapter);
        spinner_album.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.albumSelected(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        handler.postDelayed(() ->presenter.resume(), 500);

    }




    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void scrollToTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void clearAlbums() {
        albumAdapter.clear();
    }

    @Override
    public void addAlbums(@NotNull List<Album> items) {
        ArrayList<String> folders = new ArrayList<>();
        for(int i =0;i<items.size();i++){
            folders.add(items.get(i).getFolderName());
        }
        albumAdapter.addAll(folders);
        albumAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearImages() {
        adapter.clear();
    }

    @Override
    public void addImages(@NotNull List<Image> items) {
        progres_chargin_photos.setVisibility(View.GONE);
        adapter.addAll(items);
    }

    @Override
    public void showPermissionDenied() {

    }

    @Override
    public void hidePermissionDenied() {

    }

    @Override
    public void requestPermissions() {

    }

    @Override
    public void finishPickImages(@NotNull List<Image> items) {
        Bundle b = new Bundle();
        b.putStringArrayList("PATH_SELECTED",paths);
        if(listener!=null){
            listener.change_fragment_parameter(FragmentElement.INSTANCE_CROP_IMAGE,b);
        }
    }

    @Override
    public void finish() {

    }

    @Override
    public void onItemClick(@NotNull ViewGroup parent, @NotNull View view, int position, @NotNull Image item, boolean selectable) {
        if (!selectable) return ;
       if(adapter.getSelectedImages().size()>0){
           adapter.updateItemView(position, config.getMaxCount());
       }else{
           Bundle b = new Bundle();
           ArrayList<String> uri = new ArrayList<>();
           uri.add(item.getPath());
           b.putStringArrayList("PATH_SELECTED",uri);
           if(listener!=null){
               Log.e("RESULT_CLIXK","!=null");
               if(App.read(PREFERENCES.FROM_PICKER,"PROFILE").equals("PROFILE")){
                   Log.e("RESULT_CLIXK","profile");
                   if(is_from_profile ==2)
                       listener.change_fragment_parameter(FragmentElement.INSTANCE_HISTORY_FOTO_PICKED,b);
                   else
                      uploas_listener.setResultForOtherChanges(uri.get(0));
               }else{
                   Log.e("RESULT_CLIXK","posts");
                   listener.change_fragment_parameter(FragmentElement.INSTANCE_CROP_IMAGE,b);
               }

           }else{
               Log.e("RESULT_CLIXK","listener_nulo");
           }
       }
      return  ;
      //  cropLayout.setUri(Uri.parse(adapter.get_uri(position)));
    }

    @Override
    public boolean onItemLongClickListener(@NotNull ViewGroup parent, @NotNull View view, int position, @NotNull Image item, boolean selectable) {
        if (!selectable) return false;
        adapter.updateItemView(position, config.getMaxCount());
        return true;
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
        uploas_listener =(uploads)context;
    }

    @Override
    public void show_feed(ArrayList<PostBean> data, ArrayList<HistoriesBean> data_stories) {

    }

    @Override
    public void peticion_error() {

    }
}

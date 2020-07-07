package com.bunizz.instapetts.fragments.share_post.Picker.video;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.fragments.share_post.Picker.image.ImageListRecyclerViewAdapter;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;

import com.bunizz.instapetts.utils.imagePicker.data.AlbumVideo;
import com.bunizz.instapetts.utils.imagePicker.data.Config;
import com.bunizz.instapetts.utils.imagePicker.data.Image;
import com.bunizz.instapetts.utils.imagePicker.data.Video;
import com.bunizz.instapetts.utils.imagePicker.helper.EmptySupportedRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPickerVideo extends Fragment implements   VideoPickerContract.View,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        VideoListRecyclerViewAdapter.OnItemClickListener{

    String PATH_TEMP="-";

    @BindView(R.id.crop_now)
    Button crop_now;

    @BindView(R.id.spinner_album)
    AppCompatSpinner spinner_album;



    @BindView(R.id.recycler_view)
    EmptySupportedRecyclerView recyclerView;

    VideoListRecyclerViewAdapter adapter;
    VideoPickerPresenter presenter;
    ArrayAdapter albumAdapter;
    Config config;
    boolean IS_CROPED_IMAGE_FINISH = false;

    String PATH_after ="";

    ArrayList<String> paths = new ArrayList<>();

    changue_fragment_parameters_listener listener;

  //  private enum class Tag { SPINNER_ALBUM, IMAGE, BUTTON_SETTING };

    public static FragmentPickerVideo newInstance() {
        return new FragmentPickerVideo();
    }
   int is_from_profile =0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new VideoListRecyclerViewAdapter(getContext());
        config = new Config();

        config.setPackageName(getContext().getPackageName());
        albumAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);
        presenter = new VideoPickerPresenter(this,getContext(),config);
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
        presenter.resume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_videopicker_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        //recyclerView.setTag(ImagePickerFragment.IMAGE);
        recyclerView.setAdapter(adapter);
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
    public void addAlbums(@NotNull List<AlbumVideo> items) {
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
    public void addImages(@NotNull List<Video> items) {
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
    public void finishPickImages(@NotNull List<Video> items) {
        Bundle b = new Bundle();
        ArrayList<String> uri_video = new ArrayList<>();
        uri_video.add(items.get(0).getPath());
        b.putStringArrayList("data_pahs",uri_video);
        b.putInt("is_video",1);
        if(listener!=null){
            listener.change_fragment_parameter(FragmentElement.INSTANCE_SHARE,b);
        }
    }

    @Override
    public void finish() {

    }

    @Override
    public void onItemClick(@NotNull ViewGroup parent, @NotNull View view, int position, @NotNull Video item, boolean selectable) {
        Bundle b = new Bundle();

        if((Integer.valueOf(item.getDuration())/1000) < 1){
            Log.e("TAM_VIDEO","EL VIDEO ES CORTO ASI LO SUBO");
            ArrayList url_final = new ArrayList();
            url_final.add(item.getPath());
            b.putStringArrayList("data_pahs",url_final);
            b.putInt("is_video",1);
            listener.change_fragment_parameter(FragmentElement.INSTANCE_SHARE, b);
        }else {
            Log.e("TAM_VIDEO","EL VIDEO ES LARGO LO CORTO");
            b.putString("PATH_TO_CROP",item.getPath());
            b.putInt("is_video",1);
            Log.e("DURATION","----->"+ item.getDuration());
            b.putInt("DURATION",Integer.valueOf(item.getDuration())/1000);
            listener.change_fragment_parameter(FragmentElement.INSTANCE_CROP_VIDEO, b);
        }
        if (!selectable) return;
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }

}

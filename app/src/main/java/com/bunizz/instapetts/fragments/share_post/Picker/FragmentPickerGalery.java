package com.bunizz.instapetts.fragments.share_post.Picker;

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
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
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
    @BindView(R.id.crop_view)
    CropLayout cropLayout;

    @BindView(R.id.crop_now)
    Button crop_now;

    @BindView(R.id.spinner_album)
    AppCompatSpinner spinner_album;



    @BindView(R.id.recycler_view)
    EmptySupportedRecyclerView recyclerView;

    ImageListRecyclerViewAdapter adapter;
    ImagePickerPresenter presenter;
    ArrayAdapter albumAdapter;
    Config config;
    boolean IS_CROPED_IMAGE_FINISH = false;

    String PATH_after ="";

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
        presenter.resume();
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
        cropLayout.addOnCropListener(new OnCropListener() {
            @Override
            public void onSuccess(@NotNull Bitmap bitmap) {
                if(saveImage(bitmap,"Instapetts","Instapetts_", Bitmap.CompressFormat.JPEG)){
                    if(IS_CROPED_IMAGE_FINISH) {
                        presenter.saveSelected(adapter.getSelectedImages());
                    }else{
                        Log.e("FINISH_STATUS","NO A FINALIZADO");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Exception e) {

            }
        });
        crop_now.setOnClickListener(view1 -> {
            if (cropLayout.isOffFrame()) {
                return;
            }else{
                IS_CROPED_IMAGE_FINISH = true;
                cropLayout.crop();
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
    }


    @Override
    public void show_feed(ArrayList<PetBean> data) {

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
        cropLayout.setUri(Uri.parse(items.get(0).getPath()));
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
        b.putStringArrayList("data_pahs",paths);
        if(listener!=null){
            listener.change_fragment_parameter(FragmentElement.INSTANCE_SHARE,b);
        }
    }

    @Override
    public void finish() {

    }

    @Override
    public void onItemClick(@NotNull ViewGroup parent, @NotNull View view, int position, @NotNull Image item, boolean selectable) {
        if (!selectable) return;
        cropLayout.setUri(Uri.parse(adapter.get_uri(position)));
    }

    @Override
    public boolean onItemLongClickListener(@NotNull ViewGroup parent, @NotNull View view, int position, @NotNull Image item, boolean selectable) {
        if (!selectable) return false;
        if(PATH_after.length()>2){
            Log.e("CORTO_ANETERIOR","si");
            cropLayout.crop();
        }
        adapter.updateItemView(position, config.getMaxCount());
        PATH_after = adapter.get_uri(position);
        cropLayout.setUri(Uri.parse(PATH_after));
        return true;
    }

    public  boolean saveImage(Bitmap bitmap, String folderName, String filename, Bitmap.CompressFormat compressFormat) {
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
            paths.add(PATH_TEMP);
            try {
                Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri fileContentUri = Uri.fromFile(file);
                mediaScannerIntent.setData(fileContentUri);
                getContext().sendBroadcast(mediaScannerIntent);
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




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }

}

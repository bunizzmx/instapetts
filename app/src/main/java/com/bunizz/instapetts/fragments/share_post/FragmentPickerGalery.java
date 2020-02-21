package com.bunizz.instapetts.fragments.share_post;

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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.utils.crop.CropLayout;
import com.bunizz.instapetts.utils.crop.OnCropListener;
import com.bunizz.instapetts.utils.imagePicker.data.Album;
import com.bunizz.instapetts.utils.imagePicker.data.Config;
import com.bunizz.instapetts.utils.imagePicker.data.Image;
import com.bunizz.instapetts.utils.imagePicker.helper.EmptySupportedRecyclerView;
import com.bunizz.instapetts.utils.imagePicker.ui.picker.ImageListRecyclerViewAdapter;
import com.bunizz.instapetts.utils.imagePicker.ui.picker.ImagePickerContract;
import com.bunizz.instapetts.utils.imagePicker.ui.picker.ImagePickerFragment;
import com.bunizz.instapetts.utils.imagePicker.ui.picker.ImagePickerPresenter;

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

  //  private enum class Tag { SPINNER_ALBUM, IMAGE, BUTTON_SETTING };

    public static FragmentPickerGalery newInstance() {
        return new FragmentPickerGalery();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ImageListRecyclerViewAdapter(getContext());
        Bundle bundle=getArguments();
        config = new Config();
        config.setMaxCount(5);
        config.setPackageName(getContext().getPackageName());
        albumAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);
        presenter = new ImagePickerPresenter(this,getContext(),config);
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
                    Toast.makeText(getContext(),"IMAGEN gUARDADA",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Exception e) {

            }
        });
        crop_now.setOnClickListener(view1 -> {
            if (cropLayout.isOffFrame()) {
                Log.e("FUERA_FRAME","true");
                // Snackbar.make(parent, R.string.error_image_is_off_of_frame, Snackbar.LENGTH_LONG).show()
                return;
            }else{
                Log.e("FUERA_FRAME","false");
            }
            cropLayout.crop();
        });

        spinner_album.setAdapter(albumAdapter);
        spinner_album.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.albumSelected(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

    }

    @Override
    public void finish() {

    }

    @Override
    public void onItemClick(@NotNull ViewGroup parent, @NotNull View view, int position, @NotNull Image item, boolean selectable) {

    }

    @Override
    public boolean onItemLongClickListener(@NotNull ViewGroup parent, @NotNull View view, int position, @NotNull Image item, boolean selectable) {
        return false;
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

}

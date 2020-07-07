package com.bunizz.instapetts.fragments.share_post.cropImages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
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
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.SelectedsImagesBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.uploads;

import com.bunizz.instapetts.utils.crop2.view.ImageCropView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageCropFragment extends Fragment {

    String PATH_TEMP="-";
    @BindView(R.id.crop_view)
    ImageCropView cropLayout;

    @BindView(R.id.liste_image_selected)
    RecyclerView liste_image_selected;

    @BindView(R.id.crop_now_selected)
    Button crop_now_selected;


    @BindView(R.id.indicator_4_3)
    CardView indicator_4_3;

    @BindView(R.id.indicator_4_4)
    CardView indicator_4_4;


    @BindView(R.id.indicator_16_9)
    CardView indicator_16_9;

    ArrayList<String> array_list_cropes = new ArrayList<>();
    int CURRENT_INDEX=0;
    int IS_FROM_CAMERA = 0;

    uploads listener_uploads;
    int INDEX_IMAGE=0;

    ArrayAdapter albumAdapter;
    boolean IS_CROPED_IMAGE_FINISH = false;

    @OnClick(R.id.changue_to_4_3)
    void changue_to_4_3()
    {
      cropLayout.setAspectRatio(4,3);//1f,Uri.parse(paths.get(CURRENT_INDEX)));
        indicator_4_3.setVisibility(View.VISIBLE);
        indicator_4_4.setVisibility(View.GONE);
        indicator_16_9.setVisibility(View.GONE);
    }

    @OnClick(R.id.changue_to_4_4)
    void changue_to_4_4()
    {
        cropLayout.setAspectRatio(1,1);//0.5f,Uri.parse(paths.get(CURRENT_INDEX)));
        indicator_4_3.setVisibility(View.GONE);
        indicator_4_4.setVisibility(View.VISIBLE);
        indicator_16_9.setVisibility(View.GONE);
    }

    @OnClick(R.id.changue_to_16_9)
    void changue_to_16_9()
    {
        cropLayout.setAspectRatio(16,9);//0.5f,Uri.parse(paths.get(CURRENT_INDEX)));
        indicator_4_3.setVisibility(View.GONE);
        indicator_4_4.setVisibility(View.GONE);
        indicator_16_9.setVisibility(View.VISIBLE);
    }


    String PATH_after ="";
    ArrayList<SelectedsImagesBean> seleccionadas = new ArrayList<>();
    ArrayList<String> paths = new ArrayList<>();
    ImageSelectedAdapter adapter;
    changue_fragment_parameters_listener listener;

  //  private enum class Tag { SPINNER_ALBUM, IMAGE, BUTTON_SETTING };

    public static ImageCropFragment newInstance() {
        return new ImageCropFragment();
    }


   int is_from_profile =0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            is_from_profile = bundle.getInt("FROM_PROFILE");
            paths = bundle.getStringArrayList("PATH_SELECTED");
            IS_FROM_CAMERA  = bundle.getInt("IS_FROM_CAMERA");
            Log.e("FROM_PROFILE","--->" + is_from_profile);
        }
        adapter = new ImageSelectedAdapter(getContext());
        for(int i =0;i<paths.size();i++){
            seleccionadas.add(new SelectedsImagesBean(paths.get(i),false));
        }
        adapter.setSeleteds(seleccionadas);
        INDEX_IMAGE = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("ONRESUME","----si");
    }

    public void update_croper(){
        Bundle bundle=getArguments();
        if(bundle!=null){
            is_from_profile = bundle.getInt("FROM_PROFILE");
            paths = bundle.getStringArrayList("PATH_SELECTED");
            Log.e("FROM_PROFILE","--->" + is_from_profile);
            if(cropLayout!=null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cropLayout.setImageFilePath(paths.get(0));
                        if(adapter!=null){

                            for(int i =0;i<paths.size();i++){
                                seleccionadas.add(new SelectedsImagesBean(paths.get(i),false));
                            }
                            adapter.setSeletedsnew(seleccionadas);
                        }
                    }
                });

            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_imagepicker_picker_crop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        liste_image_selected.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        liste_image_selected.setAdapter(adapter);
        /*(new OnCropListener() {
            @Override
            public void onSuccess(@NotNull Bitmap bitmap) {
                if(saveImage(bitmap,"Instapetts","Instapetts_", Bitmap.CompressFormat.JPEG)){
                    if(IS_FROM_CAMERA ==1){
                        File file = new File(paths.get(0));
                        try {
                            if (file.delete()) {
                                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(paths.get(0)))));
                            }
                        }catch (Exception e){}
                    }
                    if(CURRENT_INDEX < paths.size()-1){
                        if(CURRENT_INDEX==paths.size()-1){
                            Log.e("lkjhdfjkdsh","POST");
                            Bundle b = new Bundle();
                            b.putStringArrayList("data_pahs",array_list_cropes);
                            listener.change_fragment_parameter(FragmentElement.INSTANCE_SHARE,b);
                        }else{
                            CURRENT_INDEX ++;
                            if(CURRENT_INDEX == paths.size()-1)
                              crop_now_selected.setText("Finalizar");

                            cropLayout.setUri(Uri.parse(paths.get(CURRENT_INDEX)));
                        }

                    }else{
                        if (App.read(PREFERENCES.FROM_PICKER, "PROFILE").equals("PROFILE")) {
                            Log.e("lkjhdfjkdsh","PROFILE");
                            listener_uploads.setResultForOtherChanges(array_list_cropes.get(0));
                        }else{
                            Log.e("lkjhdfjkdsh","POST");
                            Bundle b = new Bundle();
                            b.putStringArrayList("data_pahs",array_list_cropes);
                            listener.change_fragment_parameter(FragmentElement.INSTANCE_SHARE,b);
                            CURRENT_INDEX =0;
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Exception e) {

            }
        });*/
        indicator_4_4.setVisibility(View.VISIBLE);
        cropLayout.setImageFilePath(paths.get(CURRENT_INDEX));
        if (paths.size() == 1){
            crop_now_selected.setText("Finalizar");
        }else{
            crop_now_selected.setText("Siguiente");
        }
        crop_now_selected.setOnClickListener(view1 -> {
            if(CURRENT_INDEX < paths.size()-1){
                adapter.update_croped_item(CURRENT_INDEX);
                saveImage(cropLayout.getCroppedImage(),"Instapetts","Instapetts_", Bitmap.CompressFormat.JPEG);
            }else if(CURRENT_INDEX == paths.size()-1){
                saveImage(cropLayout.getCroppedImage(),"Instapetts","Instapetts_", Bitmap.CompressFormat.JPEG);
            }
        });

    }



    public  boolean saveImage(Bitmap bitmap, String folderName, String filename, Bitmap.CompressFormat compressFormat) {
        if(is_from_profile == 1){
            filename = filename + App.read(PREFERENCES.UUID,"INVALID");
        }else{
            filename = filename + UUID.randomUUID();
        }

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
            array_list_cropes.add(PATH_TEMP);
            try {
                Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri fileContentUri = Uri.fromFile(file);
                mediaScannerIntent.setData(fileContentUri);
                getContext().sendBroadcast(mediaScannerIntent);

                if(CURRENT_INDEX < paths.size()-1){
                    if(CURRENT_INDEX==paths.size()-1){
                        Log.e("lkjhdfjkdsh","POST");
                        Bundle b = new Bundle();
                        b.putStringArrayList("data_pahs",array_list_cropes);
                        listener.change_fragment_parameter(FragmentElement.INSTANCE_SHARE,b);
                    }else{
                        CURRENT_INDEX ++;
                        if(CURRENT_INDEX == paths.size()-1)
                            crop_now_selected.setText("Finalizar");
                        cropLayout.setImageFilePath(paths.get(CURRENT_INDEX));
                    }

                }else{
                    if (App.read(PREFERENCES.FROM_PICKER, "PROFILE").equals("PROFILE")) {
                        Log.e("lkjhdfjkdsh","PROFILE");
                        listener_uploads.setResultForOtherChanges(array_list_cropes.get(0));
                    }else{
                        Log.e("lkjhdfjkdsh","POST");
                        Bundle b = new Bundle();
                        b.putStringArrayList("data_pahs",array_list_cropes);
                        listener.change_fragment_parameter(FragmentElement.INSTANCE_SHARE,b);
                        CURRENT_INDEX =0;
                    }
                }


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
            listener_uploads =(uploads)context;
    }


    public class ImageSelectedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<SelectedsImagesBean> seleteds = new ArrayList<>();
        Context context;

        public ImageSelectedAdapter(Context context) {
            this.context = context;
        }

        public ArrayList<SelectedsImagesBean> getSeleteds() {
            return seleteds;
        }

        public void update_croped_item(int index){
            this.seleteds.get(index).setIs_selected(true);
            notifyDataSetChanged();
        }

        public void setSeleteds(ArrayList<SelectedsImagesBean> seleteds) {
            this.seleteds = seleteds;
        }
        public void setSeletedsnew(ArrayList<SelectedsImagesBean> seleteds) {
            this.seleteds = seleteds;
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_image,parent,false);
           return  new ImageSelectedHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ImageSelectedHolder h = (ImageSelectedHolder)holder ;
            Glide.with(context).load(seleteds.get(position).getPath()).into(h.image_selected);
            if(seleteds.get(position).isIs_selected()){
                h.image_croped.setVisibility(View.VISIBLE);
            }else
                h.image_croped.setVisibility(View.GONE);

        }

        @Override
        public int getItemCount() {
            return seleteds.size();
        }

        public class ImageSelectedHolder extends RecyclerView.ViewHolder{
            ImageView image_selected;
            RelativeLayout image_croped;
            public ImageSelectedHolder(@NonNull View itemView) {
                super(itemView);
                image_selected = itemView.findViewById(R.id.image_selected);
                image_croped = itemView.findViewById(R.id.image_croped);
            }
        }
    }
}

package com.bunizz.instapetts.fragments.share_post.Share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.listeners.chose_pet_listener;
import com.bunizz.instapetts.listeners.remove_litener;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.services.ImagePostsService;
import com.bunizz.instapetts.utils.dilogs.DialogNoPets;
import com.bunizz.instapetts.utils.dilogs.DialogShosePet;
import com.bunizz.instapetts.web.CONST;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bunizz.instapetts.web.CONST.BASE_URL_BUCKET;

public class FragmentSharePost extends Fragment implements  SharePostContract.View{



    @BindView(R.id.list_image_selected)
    RecyclerView list_image_selected;

    @BindView(R.id.location_user)
    TextView location_user;

    @BindView(R.id.label_contains_pet)
    TextView label_contains_pet;



    @BindView(R.id.caracteres_share_post)
    TextView caracteres_share_post;

    uploads listener;
    @BindView(R.id.description_post)
    EditText description_post;
    String concat_paths="";
    PetHelper helper;
    ArrayList<PetBean> pets_cuerrent = new ArrayList<>();

    @BindView(R.id.check_location)
    Switch check_location;

    @BindView(R.id.check_apagar_comments)
    Switch check_apagar_comments;
    String ULTIMATE_IMAGE_THUMBH="-";

    PostBean post ;
    int DURACION =0;
    String ASPECT="";
    int CONTAINS_A_PET =0;

    @OnClick(R.id.share_post_pet)
    void share_post_pet()
    {
        if(pets_cuerrent.size()> 0) {
            DialogShosePet dialogShosePet = new DialogShosePet(getActivity());
            dialogShosePet.setPetBeans(pets_cuerrent);
            dialogShosePet.setListener(new chose_pet_listener() {
                @Override
                public void chose(String url_foto, int id_pet, String name_pet) {
                    post = new PostBean();
                    post.setName_pet(name_pet);
                    post.setId_pet(id_pet);
                    post.setUrl_photo_pet(url_foto);
                    post.setCp(App.read(PREFERENCES.CP,0));
                    post.setLat(App.read(PREFERENCES.LAT,0f));
                    post.setLon(App.read(PREFERENCES.LON,0f));
                    if(check_apagar_comments.isChecked())
                        post.setCan_comment(1);
                    else
                        post.setCan_comment(0);

                    paths.clear();
                    paths = adapter.get_selecteds();
                    for (int i =0;i< paths.size();i++){
                        String splits[] = paths.get(i).split("/");
                        int index = splits.length;
                        if(i==0) {
                            if(is_video == 1) {
                                concat_paths = App.getInstance().make_uri_video_hls(splits[index - 1],ASPECT);
                            }
                            else
                                concat_paths = App.getInstance().make_uri_bucket_posts(splits[index - 1]);
                        }
                        else {
                            if(is_video == 1)
                                concat_paths = App.getInstance().make_uri_video_hls(splits[index - 1],ASPECT);
                            else
                                concat_paths += "," + App.getInstance().make_uri_bucket_posts(splits[index - 1]);
                        }
                        if(is_video !=1) {
                            ULTIMATE_IMAGE_THUMBH = App.getInstance().make_uri_bucket_posts_thumbh(splits[index - 1]);
                        }
                    }



                    Log.e("POSICIONES","--->" +adapter.get_selecteds().size());
                    if(is_video==1) {
                        post.setDuracion(DURACION);
                        post.setAspect(ASPECT);
                        beginUploadInBackground(adapter.get_selecteds(), true);
                    }
                    else {
                        post.setDuracion(0);
                        post.setAspect("-");
                        post.setThumb_video(ULTIMATE_IMAGE_THUMBH);
                        beginUploadInBackground(adapter.get_selecteds(), false);
                    }
                }

                @Override
                public void request_no_pets() {

                }
            });
            if (dialogShosePet != null) {
                dialogShosePet.show();
            }
        }else{
            DialogNoPets dialogNoPets = new DialogNoPets(getActivity());
            dialogNoPets.setListener(new chose_pet_listener() {
                @Override
                public void chose(String url_foto, int id_pet, String name_pet) {

                }

                @Override
                public void request_no_pets() {
                    listener.new_pet();
                }
            });
            dialogNoPets.show();
        }
    }
    ArrayList<String> paths = new ArrayList<>();
    SharePostPresenter presenter;
    int is_video =0;
    ListSelectedAdapter adapter;
    public static FragmentSharePost newInstance() {
        return new FragmentSharePost();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ListSelectedAdapter(getContext());
        adapter.setListener_remove(new remove_litener() {
            @Override
            public void remove(int size) {
                paths.remove(size);
                if(paths.size() == 0){
                    getActivity().onBackPressed();
                }
            }
        });
        presenter = new SharePostPresenter(this, getContext());
        Bundle bundle=getArguments();
        helper = new PetHelper(getContext());
        if(bundle!=null){
            paths.addAll(bundle.getStringArrayList("data_pahs"));
            CONTAINS_A_PET = bundle.getInt("CONTAINS_A_PET");
            is_video = bundle.getInt("is_video");
            DURACION = bundle.getInt(BUNDLES.VIDEO_DURATION,30);
            ASPECT =   bundle.getString(BUNDLES.VIDEO_ASPECT);
        }
        adapter.setData(paths);
        pets_cuerrent = helper.getMyPets();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_image_selected.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        list_image_selected.setAdapter(adapter);
        if(App.read(PREFERENCES.ALLOW_LOCATION_POST,true))
            check_location.setChecked(true);
        else
            check_location.setChecked(false);

        check_location.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                App.write(PREFERENCES.ALLOW_LOCATION_POST,true);
            else
                App.write(PREFERENCES.ALLOW_LOCATION_POST,false);
        });

        description_post.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                caracteres_share_post.setText(description_post.getText().toString().length() +  "/" + "180");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        presenter.getLocation();
        if(CONTAINS_A_PET == 0){
            label_contains_pet.setVisibility(View.VISIBLE);
            label_contains_pet.setTextColor(getContext().getResources().getColor(R.color.primary));
            label_contains_pet.setText("TUS FOTOS NO CONTIENEN MASCOTAS POR LO QUE SRAN MENOS VISIBLES");
        }else{
            label_contains_pet.setVisibility(View.GONE);
        }
    }

    public void refresh_list(){
        if(list_image_selected!=null && adapter!=null){
            paths.clear();
            Bundle bundle=getArguments();
            paths.addAll(bundle.getStringArrayList("data_pahs"));
            CONTAINS_A_PET = bundle.getInt("CONTAINS_A_PET");
            adapter.setData(paths);
            if(CONTAINS_A_PET == 0){
                label_contains_pet.setVisibility(View.VISIBLE);
                label_contains_pet.setTextColor(getContext().getResources().getColor(R.color.primary));
                label_contains_pet.setText("TUS FOTOS NO CONTIENEN MASCOTAS POR LO QUE SRAN MENOS VISIBLES");
            }else{
                label_contains_pet.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void postStatus(boolean status) {
        if(status){
            if(getActivity()!=null){
                getActivity().finish();
            }
        }else{
            if(getContext()!=null)
            Toast.makeText(getContext(),"FALLO AL PUBLICAR INTENTA DE NUEVO",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showLocation(String address) {
        if(!address.equals("INVALID"))
        location_user.setText(address);
    }

    private void beginUploadInBackground(ArrayList<String> filePaths,boolean is_video) {
        if (filePaths == null) {
            Toast.makeText(getContext(), "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Context context = getContext().getApplicationContext();
        Intent intent = new Intent(context, ImagePostsService.class);
        intent.putStringArrayListExtra(ImagePostsService.INTENT_KEY_NAME, filePaths);
        intent.putExtra(BUNDLES.VIDEO_ASPECT, ASPECT);
        intent.putExtra(BUNDLES.NOTIFICATION_TIPE,0);
        if(is_video) {
            String uri_tuhmbh="";
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(filePaths.get(0), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);//MediaStore.Video.Thumbnails.MICRO_KIND
            if (bMap != null) {
                try {
                    File newfile = savebitmap(bMap,filePaths.get(0));
                    uri_tuhmbh = String.valueOf(newfile.getPath());
                    post.setThumb_video(App.getInstance().make_uri_bucket_thumbh_video(uri_tuhmbh));
                } catch (IOException e) {
                    Log.e("SAVE_IMAGE","ERROR : " + e.getMessage());
                    e.printStackTrace();
                }
            }
            Log.e("EXTRA_THUMBH " ,"-->"+uri_tuhmbh);
            intent.putExtra(BUNDLES.PHOTO_TUMBH,uri_tuhmbh);
            intent.putExtra(BUNDLES.POST_TYPE, 1);
            send_post();
        }
        else {
            if(filePaths.size() == 1){
                App.write(PREFERENCES.URI_TEMP_SMOOT,filePaths.get(0));
            }else{
                App.write(PREFERENCES.URI_TEMP_SMOOT,filePaths.get(filePaths.size()-1));
            }
            intent.putExtra(BUNDLES.POST_TYPE, 0);
            send_post();
        }

        intent.putExtra(ImagePostsService.INTENT_TRANSFER_OPERATION, ImagePostsService.TRANSFER_OPERATION_UPLOAD);
        context.startService(intent);
        listener.onImageProfileUpdated("PROFILE_PHOTO");
    }


    public  File savebitmap(Bitmap bmp,String name_video) throws IOException {

        File file = new File(getContext().getApplicationContext().getCacheDir() + File.separator + "THUMBS_VIDEOS");
        if (!file.exists()) {
            Log.e("CREO_CARPETA_VIDEO",getContext().getApplicationContext().getCacheDir() + File.separator + "THUMBS_VIDEOS");
            file.mkdirs();
        }


        Log.e("SAVE_IMAGE","ON CAHCE" + name_video);
        String splits[] = name_video.split("/");
        String name_video_complete = splits[splits.length-1];
        Log.e("SAVE_IMAGE","--> C : " + name_video_complete);
        Log.e("SAVE_IMAGE","--> A : " + name_video_complete.replace(".mp4",""));
        String name_sin_dot = name_video_complete.replace(".mp4","");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Log.e("SAVE_IMAGE","--> SIN DOT : "+ name_sin_dot);

        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File( getContext().getApplicationContext().getCacheDir() + File.separator + "THUMBS_VIDEOS"
                + File.separator + name_sin_dot +".jpg");
        f.createNewFile();
        String NAME_temph = f.getAbsolutePath();
        App.write(PREFERENCES.URI_TEMP_SMOOT,NAME_temph);
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener =(uploads)context;
    }


    void send_post(){
        post.setDescription(description_post.getText().toString());
        post.setName_user(App.read(PREFERENCES.NAME_USER, "INVALID"));
        post.setUrl_photo_user(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH, "INVALID"));
        post.setUrls_posts(concat_paths);
        post.setUuidbucket("xx");
        post.setUuid(App.read(PREFERENCES.UUID, "INVALID"));
        post.setDate_post(App.formatDateGMT(new Date()));
        post.setTarget(WEBCONSTANTS.NEW);
        post.setType_post(is_video);
        if(App.read(PREFERENCES.ALLOW_LOCATION_POST,true))
            post.setAddress(App.read(PREFERENCES.ADDRESS_USER,"INVALID"));
        post.setId_usuario(App.read(PREFERENCES.ID_USER_FROM_WEB, 0));
        if(post.getId_pet() == - 999){
            presenter.sendPostHelpPet(post);
        }else{
            presenter.sendPost(post);
        }

    }
}


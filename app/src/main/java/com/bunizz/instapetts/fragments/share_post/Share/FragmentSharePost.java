package com.bunizz.instapetts.fragments.share_post.Share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.listeners.chose_pet_listener;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.services.ImagePostsService;
import com.bunizz.instapetts.utils.dilogs.DialogNoPets;
import com.bunizz.instapetts.utils.dilogs.DialogShosePet;
import com.bunizz.instapetts.web.CONST;

import java.util.ArrayList;
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
    uploads listener;
    @BindView(R.id.description_post)
    EditText description_post;
    String concat_paths="";
    PetHelper helper;
    ArrayList<PetBean> pets_cuerrent = new ArrayList<>();

    @BindView(R.id.check_location)
    Switch check_location;



    @OnClick(R.id.share_post_pet)
    void share_post_pet()
    {
        if(pets_cuerrent.size()> 0) {
            DialogShosePet dialogShosePet = new DialogShosePet(getActivity());
            dialogShosePet.setPetBeans(pets_cuerrent);
            dialogShosePet.setListener(new chose_pet_listener() {
                @Override
                public void chose(String url_foto, int id_pet, String name_pet) {
                    beginUploadInBackground(paths);
                    PostBean post = new PostBean();
                    post.setName_pet(name_pet);
                    post.setDescription(description_post.getText().toString());
                    post.setName_user(App.read(PREFERENCES.NAME_USER, "INVALID"));
                    post.setUrl_photo_pet(url_foto);
                    post.setUrl_photo_user(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH, "INVALID"));
                    post.setUrls_posts(concat_paths);
                    post.setUuidbucket("xx");
                    post.setUuid(App.read(PREFERENCES.UUID, "INVALID"));
                    post.setDate_post(App.formatDateGMT(new Date()));
                    post.setTarget("NEW");
                    post.setId_pet(id_pet);
                    if(App.read(PREFERENCES.ALLOW_LOCATION_POST,true))
                    post.setAddress(App.read(PREFERENCES.ADDRESS_USER,"INVALID"));
                    post.setId_usuario(App.read(PREFERENCES.ID_USER_FROM_WEB, 0));
                    presenter.sendPost(post);
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
        presenter = new SharePostPresenter(this, getContext());
        Bundle bundle=getArguments();
        helper = new PetHelper(getContext());
        if(bundle!=null){
            paths.addAll(bundle.getStringArrayList("data_pahs"));
            is_video = bundle.getInt("is_video");
            for (int i =0;i< paths.size();i++){
                String splits[] = paths.get(i).split("/");
                int index = splits.length;
                if(i==0)
                    concat_paths =App.getInstance().make_uri_bucket_posts(splits[index-1]);
                else
                    concat_paths += "," + App.getInstance().make_uri_bucket_posts(splits[index-1]);

                Log.e("URL_SIMPLE","-->" + splits[index-1]);
            }
            Log.e("URL_SIMPLE","FInal : " + concat_paths);
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
        presenter.getLocation();
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

    private void beginUploadInBackground(ArrayList<String> filePaths) {
        if (filePaths == null) {
            Toast.makeText(getContext(), "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Context context = getContext().getApplicationContext();
        Intent intent = new Intent(context, ImagePostsService.class);
        intent.putStringArrayListExtra(ImagePostsService.INTENT_KEY_NAME, filePaths);
        intent.putExtra(BUNDLES.NOTIFICATION_TIPE,0);
        intent.putExtra(ImagePostsService.INTENT_TRANSFER_OPERATION, ImagePostsService.TRANSFER_OPERATION_UPLOAD);
        context.startService(intent);
        listener.onImageProfileUpdated();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener =(uploads)context;
    }
}


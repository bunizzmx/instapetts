package com.bunizz.instapetts.fragments.login.first_user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.web.CONST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentFirstUser extends Fragment {



    change_instance listener;
    uploads listener_uploads;

    @BindView(R.id.configure_name)
    AutoCompleteTextView configure_name;

    @BindView(R.id.descripcion_user)
    AutoCompleteTextView descripcion_user;

    @BindView(R.id.image_userd_edit)
    ImageView image_userd_edit;


    String URL_UPDATED="INVALID";
    String URL_LOCAL="INVALID";

    @SuppressLint("MissingPermission")
    @OnClick(R.id.change_photo)
    void change_photo()
    {
        App.write(PREFERENCES.FROM_PICKER,"PROFILE");
        listener_uploads.onImageProfileUpdated();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.save_info_perfil)
    void save_info_perfil()
    {
        String URI_FINAL  =App.getInstance().make_uri_bucket_profile();
        App.write(PREFERENCES.DESCRIPCCION,descripcion_user.getText().toString());
        App.write(PREFERENCES.FOTO_PROFILE_USER,URI_FINAL);
        App.write(PREFERENCES.NAME_USER,configure_name.getText().toString());
        Bundle b = new Bundle();
        b.putString("DESCRIPCION",descripcion_user.getText().toString());
        b.putString("PHOTO",URI_FINAL);
        b.putString("PHOTO_LOCAL",URL_LOCAL);
        listener_uploads.UpdateProfile(b);
    }



    public static FragmentFirstUser newInstance() {
        return new FragmentFirstUser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(  App.read(PREFERENCES.NAME_USER,"-").equals("-")){
            configure_name.setHint("@Un usuario");
        }else{
            configure_name.setText(App.read(PREFERENCES.NAME_USER,"-"));
        }
    }


    public void change_image_profile(String url){
        URL_LOCAL = url;
        String splits[] = url.split("/");
        int index = splits.length;
        URL_UPDATED = splits[index - 1];
        Glide.with(getContext()).load(url).into(image_userd_edit);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
        listener_uploads =(uploads)context;
    }
}

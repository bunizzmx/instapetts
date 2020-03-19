package com.bunizz.instapetts.fragments.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.open_sheet_listener;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.utils.tabs.TabType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bunizz.instapetts.web.CONST.BASE_URL_BUCKET;

public class FragmentEditProfileUser extends Fragment {

    @SuppressLint("MissingPermission")
    @OnClick(R.id.change_photo)
    void change_photo()
    {
        App.write(PREFERENCES.FROM_PICKER,"PROFILE");
       listener.onImageProfileUpdated();
    }

    @BindView(R.id.image_userd_edit)
    ImageView image_userd_edit;


    @BindView(R.id.configure_name)
    AutoCompleteTextView configure_name;

    @BindView(R.id.descripcion_user)
    AutoCompleteTextView descripcion_user;

    @BindView(R.id.name_user)
    TextView name_user;

    @BindView(R.id.save_info_perfil)
    Button save_info_perfil;
    String URL_UPDATED="INVALID";
    String URL_LOCAL="INVALID";
    @SuppressLint("MissingPermission")
    @OnClick(R.id.save_info_perfil)
    void save_info_perfil()
    {
        String URI_FINAL  = BASE_URL_BUCKET +"" + URL_UPDATED;
        App.write(PREFERENCES.DESCRIPCCION,descripcion_user.getText().toString());
        App.write(PREFERENCES.FOTO_PROFILE_USER,URI_FINAL);
        Bundle b = new Bundle();
        b.putString("DESCRIPCION",descripcion_user.getText().toString());
        b.putString("PHOTO",URI_FINAL);
        b.putString("PHOTO_LOCAL",URL_LOCAL);
        listener.UpdateProfile(b);
    }


    uploads listener;
    int type_config=0;

    public static FragmentEditProfileUser newInstance() {
        return new FragmentEditProfileUser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            type_config = bundle.getInt("CONFIG");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(type_config == 0){
            configure_name.setVisibility(View.INVISIBLE);
            name_user.setVisibility(View.GONE);
        }else{
            configure_name.setVisibility(View.GONE);
            name_user.setVisibility(View.VISIBLE);
            name_user.setText(App.read(PREFERENCES.NAME_USER,"USUARIO"));
        }
        if(!App.read(PREFERENCES.DESCRIPCCION,"INVALID").equals("INVALID")){
            descripcion_user.setText(App.read(PREFERENCES.DESCRIPCCION,"INVALID"));
        }
        URL_UPDATED = App.read(PREFERENCES.FOTO_PROFILE_USER,"INVALID");
        Glide.with(getContext()).load(URL_UPDATED).placeholder(getContext().getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(image_userd_edit);
        Log.e("URL_GUARDADA","-->" + URL_UPDATED);
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
        listener= (uploads) context;
    }
}

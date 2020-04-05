package com.bunizz.instapetts.fragments.wizardPets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.listeners.process_save_pet_listener;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.web.CONST;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentFinalConfigPet extends Fragment {

    change_instance_wizard listener;
    uploads uploads_listener;
    process_save_pet_listener listener_pet_config;
    String URL_PET="";

    @SuppressLint("MissingPermission")
    @OnClick(R.id.finalice_pet)
    void finalice_pet()
    {
       if(!configure_name_pet.getText().toString().isEmpty() &&  !descripcion_pet.getText().toString().isEmpty()){
           if(listener!=null){
               Log.e("PET_FINALICE",":)");
               Bundle b = new Bundle();
               b.putString(BUNDLES.NAME_PET,configure_name_pet.getText().toString());
               b.putString(BUNDLES.DESCRIPCION_PET,descripcion_pet.getText().toString());
               b.putString(BUNDLES.URL_PHOTO_PET,URL_PET);
               listener_pet_config.SaveDataPet(b,4);
               listener.onpetFinish(true);
           }else{
               Log.e("PET_FINALICE","nulo");
           }
       }else{
         if(configure_name_pet.getText().toString().isEmpty())
             Toast.makeText(getContext(),"EScribe nomvre de mascota",Toast.LENGTH_LONG).show();

         if(descripcion_pet.getText().toString().isEmpty())
               Toast.makeText(getContext(),"Que le gusta a tu mascota?",Toast.LENGTH_LONG).show();
       }

    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.change_photo_pet)
    void change_photo_pet()
    {
        uploads_listener.onImageProfileUpdated();
    }

    @BindView(R.id.configure_name_pet)
    EditText configure_name_pet;

    @BindView(R.id.descripcion_pet)
    EditText descripcion_pet;

    @BindView(R.id.image_pet_edit)
    ImagenCircular image_pet_edit;



    public static FragmentFinalConfigPet newInstance() {
        return new FragmentFinalConfigPet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_final_config_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance_wizard) context;
        uploads_listener =(uploads)context;
        listener_pet_config =(process_save_pet_listener)context;
    }

    public void change_image_profile(String url){
        URL_PET = url;
        Glide.with(getContext()).load(url).into(image_pet_edit);
    }

}

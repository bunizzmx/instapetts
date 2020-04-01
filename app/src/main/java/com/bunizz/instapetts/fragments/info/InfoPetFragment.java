package com.bunizz.instapetts.fragments.info;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.utils.ImagenCircular;

import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bunizz.instapetts.constantes.BUNDLES.PETBEAN;

public class InfoPetFragment extends Fragment {


    public static InfoPetFragment newInstance() {
        return new InfoPetFragment();
    }
   String ID_PET="";
    String NAME_PET="";
    PetHelper petHelper;
    PetBean petBean;

    @BindView(R.id.pet_name_profile)
    TextView pet_name_profile;

    @BindView(R.id.name_property_pet_profile)
    TextView name_property_pet_profile;

    @BindView(R.id.descripcion_pet_profile)
    TextView descripcion_pet_profile;

    @BindView(R.id.peso_pet_profile)
    TextView peso_pet_profile;

    @BindView(R.id.image_pet_info)
    ImagenCircular image_pet_info;
  boolean IS_ME=false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            int is_who = bundle.getInt(BUNDLES.IS_ME);
                    if(is_who == 0)
                        IS_ME = true;
                    else
                        IS_ME = false;
            petBean =  Parcels.unwrap(bundle.getParcelable(PETBEAN));
        }
        petHelper = new PetHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        pet_name_profile.setText(petBean.getName_pet());
        name_property_pet_profile.setText("@" + App.read(PREFERENCES.NAME_USER,"INVALID"));
        descripcion_pet_profile.setText(petBean.getDescripcion_pet());
        peso_pet_profile.setText(petBean.getPeso_pet() + "kg");
        Glide.with(getContext()).load(petBean.getUrl_photo()).into(image_pet_info);
    }

}


package com.bunizz.instapetts.fragments.wizardPets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.RazaBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.wizardPets.adapters.TypePetsAdapter;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.listeners.process_save_pet_listener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTypePet extends Fragment{

    @BindView(R.id.list_types_pet)
    RecyclerView list_types_pet;

    change_instance_wizard listener;
    process_save_pet_listener listener_pet_config;

    TypePetsAdapter adapter;
    ArrayList<PetTtype> petTtypes = new ArrayList<>();
    public static FragmentTypePet newInstance() {
        return new FragmentTypePet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TypePetsAdapter(getContext());
        adapter.setListener(new change_instance_wizard() {
            @Override
            public void onchange(int type_fragment, Bundle data) {
                if(listener!=null){
                    listener_pet_config.SaveDataPet(data,1);
                    listener.onchange(type_fragment,data);
                }
            }

            @Override
            public void onpetFinish(boolean pet_saved) {

            }
        });
        petTtypes.add(new PetTtype(R.drawable.ic_perro,getContext().getResources().getString(R.string.pet_type_dog),1));
        petTtypes.add(new PetTtype(R.drawable.ic_gato,getContext().getResources().getString(R.string.pet_type_cat),2));
        petTtypes.add(new PetTtype(R.drawable.ic_mascota_perico,getContext().getResources().getString(R.string.pet_type_ave),3));
        petTtypes.add(new PetTtype(R.drawable.ic_mascota_conejo,getContext().getResources().getString(R.string.pet_type_conejo),4));
        petTtypes.add(new PetTtype(R.drawable.ic_mascota_hamster,getContext().getResources().getString(R.string.pet_type_hamster),5));
        petTtypes.add(new PetTtype(R.drawable.ic_perro,getContext().getResources().getString(R.string.pet_type_otro),6));
        adapter.setPetTtypes(petTtypes);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_type_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_types_pet.setLayoutManager(new GridLayoutManager(getContext(),2));
        list_types_pet.setAdapter(adapter);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance_wizard) context;
        listener_pet_config = (process_save_pet_listener) context;
    }


}

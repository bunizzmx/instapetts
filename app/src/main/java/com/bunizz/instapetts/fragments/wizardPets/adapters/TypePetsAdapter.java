package com.bunizz.instapetts.fragments.wizardPets.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.wizardPets.PetTtype;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.dilogs.DialogOtherPet;

import java.util.ArrayList;

public class TypePetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    change_instance_wizard listener;
    ArrayList<PetTtype> petTtypes = new ArrayList<>();

    public ArrayList<PetTtype> getPetTtypes() {
        return petTtypes;
    }

    public void setPetTtypes(ArrayList<PetTtype> petTtypes) {
        this.petTtypes = petTtypes;
        notifyDataSetChanged();
    }

    public change_instance_wizard getListener() {
        return listener;
    }

    public void setListener(change_instance_wizard listener) {
        this.listener = listener;
    }

    public TypePetsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_pet,parent,false);
        return  new TypePetsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TypePetsHolder h =(TypePetsHolder)holder;
        h.name_type_pet.setText(petTtypes.get(position).getName_pet());
        h.type_pet_image.setImageDrawable(context.getResources().getDrawable(petTtypes.get(position).getId_drawable()));
        if(petTtypes.get(position).getId() == 6){
            h.type_pet_image.setOnClickListener(view -> {
                DialogOtherPet otherPet = new DialogOtherPet(context);
                otherPet.setListener(new change_instance_wizard() {
                    @Override
                    public void onchange(int type_fragment, Bundle data) {
                        otherPet.dismiss();
                        listener.onchange(FragmentElement.INSTANCE_TYPE_SEARCH_RAZA,data);
                    }
                    @Override
                    public void onpetFinish(boolean pet_saved) {

                    }
                });
                otherPet.show();
            });
        }else{
            h.type_pet_image.setOnClickListener(view -> {
                if(listener!=null){
                    Bundle b = new Bundle();
                    listener.onchange(FragmentElement.INSTANCE_TYPE_SEARCH_RAZA,b);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return petTtypes.size();
    }

    public class TypePetsHolder extends RecyclerView.ViewHolder{
        ImagenCircular type_pet_image;
        TextView name_type_pet;
        public TypePetsHolder(@NonNull View itemView) {
            super(itemView);
            type_pet_image = itemView.findViewById(R.id.type_pet_image);
            name_type_pet = itemView.findViewById(R.id.name_type_pet);
        }
    }
}

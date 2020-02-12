package com.bunizz.instapetts.fragments.wizardPets.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.utils.ImagenCircular;

public class TypePetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    change_instance_wizard listener;

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
        h.type_pet_image.setOnClickListener(view -> {
                      if(listener!=null){
                          Bundle b = new Bundle();
                          listener.onchange(FragmentElement.INSTANCE_TYPE_SEARCH_RAZA,b);
                      }
        });
        h.type_pet_image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perro));
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class TypePetsHolder extends RecyclerView.ViewHolder{
        ImagenCircular type_pet_image;
        public TypePetsHolder(@NonNull View itemView) {
            super(itemView);
            type_pet_image = itemView.findViewById(R.id.type_pet_image);
        }
    }
}

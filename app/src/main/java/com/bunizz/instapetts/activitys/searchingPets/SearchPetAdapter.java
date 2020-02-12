package com.bunizz.instapetts.activitys.searchingPets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.wizardPets.adapters.TypePetsAdapter;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.utils.ImagenCircular;

public class SearchPetAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    change_instance_wizard listener;

    public change_instance_wizard getListener() {
        return listener;
    }

    public void setListener(change_instance_wizard listener) {
        this.listener = listener;
    }

    public SearchPetAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet_searching,parent,false);
        return  new SearchPetHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchPetHolder h =(SearchPetHolder)holder;

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public class SearchPetHolder extends RecyclerView.ViewHolder{

        public SearchPetHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}

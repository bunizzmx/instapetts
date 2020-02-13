package com.bunizz.instapetts.fragments.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PropietaryBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.wizardPets.adapters.TypePetsAdapter;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.utils.ImagenCircular;

import java.util.ArrayList;

public class SearchPetAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    change_instance_wizard listener;

    ArrayList<Object> data = new ArrayList<>();

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
        notifyDataSetChanged();
    }

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
        PropietaryBean data_parsed = (PropietaryBean) data.get(position);
        h.name_pet_searching.setText(data_parsed.getNombre());
        Glide.with(context).load(data_parsed.getImage_propietary()).into(h.image_pet_searching);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SearchPetHolder extends RecyclerView.ViewHolder{
        TextView name_pet_searching;
        ImagenCircular image_pet_searching;
        public SearchPetHolder(@NonNull View itemView) {
            super(itemView);
            name_pet_searching = itemView.findViewById(R.id.name_pet_searching);
            image_pet_searching = itemView.findViewById(R.id.image_pet_searching);
        }
    }
}

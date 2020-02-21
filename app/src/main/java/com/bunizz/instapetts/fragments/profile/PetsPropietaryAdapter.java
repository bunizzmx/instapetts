package com.bunizz.instapetts.fragments.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.fragments.feed.FeedAdapterHistories;
import com.bunizz.instapetts.listeners.open_sheet_listener;
import com.bunizz.instapetts.utils.ImagenCircular;

import java.util.ArrayList;

public class PetsPropietaryAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    open_sheet_listener listener;

    ArrayList<PetBean> pets = new ArrayList<>();

    public ArrayList<PetBean> getPets() {
        return pets;
    }

    public void setPets(ArrayList<PetBean> pets) {
        this.pets = pets;
    }

    public void add_new_pet(PetBean petBean){
        this.pets.add(petBean);
        notifyDataSetChanged();
    }

    public open_sheet_listener getListener() {
        return listener;
    }

    public void setListener(open_sheet_listener listener) {
        this.listener = listener;
    }

    public PetsPropietaryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_new,parent,false);
        return  new PetsPropietaryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PetsPropietaryHolder h =(PetsPropietaryHolder)holder;
        if(position == pets.size()-1){
            h.image_pet_history_add.setOnClickListener(view -> listener.open_wizard_pet());
            h.image_pet_history_add.setVisibility(View.VISIBLE);
            h.image_pet_history.setVisibility(View.GONE);
            h.name_pet_item.setText("New Pet");
        }else{
            h.name_pet_item.setText(pets.get(position).getName_pet() + "-");
            h.image_pet_history.setOnClickListener(view -> listener.open());
            h.image_pet_history.setVisibility(View.VISIBLE);
            h.image_pet_history_add.setVisibility(View.GONE);
            Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/melove-principal/o/C18%2F15cb3831f0b9426c484d380f0ab1afac.jpg?alt=media&token=042d5974-e96c-4bcc-9fa2-f657fe2167af").into(h.image_pet_history);
        }

    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public class PetsPropietaryHolder extends RecyclerView.ViewHolder{
        ImagenCircular image_pet_history;
        ImagenCircular image_pet_history_add;
        TextView name_pet_item;
        public PetsPropietaryHolder(@NonNull View itemView) {
            super(itemView);
            image_pet_history = itemView.findViewById(R.id.image_pet_history);
            name_pet_item = itemView.findViewById(R.id.name_pet_item);
            image_pet_history_add = itemView.findViewById(R.id.image_pet_history_add);
        }
    }
}

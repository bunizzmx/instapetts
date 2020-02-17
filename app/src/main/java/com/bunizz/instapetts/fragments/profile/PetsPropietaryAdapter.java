package com.bunizz.instapetts.fragments.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.feed.FeedAdapterHistories;
import com.bunizz.instapetts.listeners.open_sheet_listener;
import com.bunizz.instapetts.utils.ImagenCircular;

public class PetsPropietaryAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    open_sheet_listener listener;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return  new PetsPropietaryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PetsPropietaryHolder h =(PetsPropietaryHolder)holder;
        Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/melove-principal/o/C18%2F15cb3831f0b9426c484d380f0ab1afac.jpg?alt=media&token=042d5974-e96c-4bcc-9fa2-f657fe2167af").into(h.image_pet_history);
        h.image_pet_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.open();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class PetsPropietaryHolder extends RecyclerView.ViewHolder{
        ImagenCircular image_pet_history;
        public PetsPropietaryHolder(@NonNull View itemView) {
            super(itemView);
            image_pet_history = itemView.findViewById(R.id.image_pet_history);
        }
    }
}

package com.bunizz.instapetts.fragments.profile.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.listeners.open_sheet_listener;
import com.bunizz.instapetts.utils.ImagenCircular;

import java.util.ArrayList;

public class PetsPropietaryAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    open_sheet_listener listener;
    boolean IS_ME=false;

    ArrayList<PetBean> pets = new ArrayList<>();

    public ArrayList<PetBean> getPets() {
        return pets;
    }

    public void setPets(ArrayList<PetBean> pets) {
        IS_ME = true;
        if (pets.size() > 0) {
            this.pets.clear();
            this.pets.addAll(pets);
            notifyDataSetChanged();
        }

    }
    public void clear(){
        this.pets.clear();
        notifyDataSetChanged();
    }
    public void setPetsforOtherUser(ArrayList<PetBean> pets) {
        Log.e("OTHER_PET","-->" +pets.size());
        IS_ME = false;
        if (pets.size() > 0) {
            this.pets.clear();
            this.pets.addAll(pets);
            notifyDataSetChanged();
        }else{
            this.pets.clear();;
            notifyDataSetChanged();
        }
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
        if(IS_ME) {
            if (position == pets.size() - 1) {
                h.image_pet_history_add.setOnClickListener(view -> listener.open_wizard_pet());
                h.image_pet_history_add.setVisibility(View.VISIBLE);
                h.image_pet_history.setVisibility(View.GONE);
                h.name_pet_item.setText(context.getString(R.string.new_pet));
                h.type_pet_icon.setVisibility(View.GONE);
            } else {
                h.type_pet_icon.setVisibility(View.VISIBLE);
                Log.e("TIPO_PETO","-->"+pets.get(position).getType_pet() + "/" + pets.get(position).getId_propietary() );
                switch (pets.get(position).getType_pet()){
                    case 1:
                        h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perro));
                    break;
                    case 2:
                        h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gato));
                        break;
                    case 3:
                        h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_perico));
                        break;
                    case 4:
                        h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_conejo));
                        break;
                    case 5:
                        h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_hamster));
                        break;
                    case 6:
                        h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_serpiente));
                        break;
                    case 7:
                        h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_otro));
                        break;
                    case 8:
                        h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pez));
                        break;
                    case 9:
                        h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cabra));
                        break;
                    default:
                        h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_otro));
                        break;

                }
                if(pets.get(position).getName_pet().length() > 12)
                    h.name_pet_item.setText(pets.get(position).getName_pet().substring(0,12).trim() + "...");
                else
                    h.name_pet_item.setText(pets.get(position).getName_pet().trim());
                h.image_pet_history.setOnClickListener(view -> listener.open(pets.get(position),0));
                h.image_pet_history.setVisibility(View.VISIBLE);
                h.image_pet_history_add.setVisibility(View.GONE);
                Glide.with(context).load(pets.get(position).getUrl_photo_tumbh())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(context.getDrawable(R.drawable.ic_holder))
                        .error(context.getResources().getDrawable(R.drawable.ic_holder)).into(h.image_pet_history);
            }
        }else{
            switch (pets.get(position).getType_pet()){
                case 1:
                    h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perro));
                    break;
                case 2:
                    h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gato));
                    break;
                case 3:
                    h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_perico));
                    break;
                case 4:
                    h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_conejo));
                    break;
                case 5:
                    h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_hamster));
                    break;
                case 6:
                    h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_serpiente));
                    break;
                case 7:
                    h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_otro));
                    break;
                case 8:
                    h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pez));
                    break;
                case 9:
                    h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cabra));
                    break;
                default:
                    h.type_pet_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_otro));
                    break;

            }
            h.type_pet_icon.setVisibility(View.VISIBLE);
            h.name_pet_item.setText(pets.get(position).getName_pet());
            h.image_pet_history.setOnClickListener(view -> listener.open(pets.get(position),1));
            h.image_pet_history.setVisibility(View.VISIBLE);
            h.image_pet_history_add.setVisibility(View.GONE);
            Glide.with(context).load(pets.get(position).getUrl_photo_tumbh())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(context.getDrawable(R.drawable.ic_holder))
                    .error(context.getResources().getDrawable(R.drawable.ic_holder)).into(h.image_pet_history);
        }

    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public class PetsPropietaryHolder extends RecyclerView.ViewHolder{
        ImageView image_pet_history,type_pet_icon;
        ImagenCircular image_pet_history_add;
        TextView name_pet_item;
        public PetsPropietaryHolder(@NonNull View itemView) {
            super(itemView);
            image_pet_history = itemView.findViewById(R.id.image_pet_history);
            name_pet_item = itemView.findViewById(R.id.name_pet_item);
            image_pet_history_add = itemView.findViewById(R.id.image_pet_history_add);
            type_pet_icon = itemView.findViewById(R.id.type_pet_icon);
        }
    }
}

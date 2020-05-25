package com.bunizz.instapetts.fragments.search.tabs.pets;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PropietaryBean;
import com.bunizz.instapetts.beans.SearchPetBean;
import com.bunizz.instapetts.beans.SearchUserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.wizardPets.adapters.TypePetsAdapter;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.searchRecentListener;
import com.bunizz.instapetts.utils.ImagenCircular;

import java.util.ArrayList;

public class SearchPetAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    changue_fragment_parameters_listener listener;
    searchRecentListener listener_recent;
    boolean HIDE_LABEL =true;
    ArrayList<Object> data = new ArrayList<>();
    boolean IS_RECENT=false;

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
        notifyDataSetChanged();
    }

   public  int get_size(){
       return  this.data.size();
    }

    public changue_fragment_parameters_listener getListener() {
        return listener;
    }

    public void setListener(changue_fragment_parameters_listener listener) {
        this.listener = listener;
    }

    public boolean isHIDE_LABEL() {
        return HIDE_LABEL;
    }

    public void setHIDE_LABEL(boolean HIDE_LABEL) {
        this.HIDE_LABEL = HIDE_LABEL;
    }


    public searchRecentListener getListener_recent() {
        return listener_recent;
    }

    public void setListener_recent(searchRecentListener listener_recent) {
        this.listener_recent = listener_recent;
    }

    public void clear(){
        this.data.clear();
        notifyDataSetChanged();
    }

    public void isRecent(boolean is_resent){
        IS_RECENT = is_resent;
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
        SearchPetBean data_parsed = (SearchPetBean) data.get(position);
        if(position==0) {
            h.search_recent_label.setVisibility(View.VISIBLE);
        }
        else {
            h.search_recent_label.setVisibility(View.GONE);
        }

        if(HIDE_LABEL == false)
            h.delete_recent.setVisibility(View.VISIBLE);
        else
            h.delete_recent.setVisibility(View.GONE);

        if(IS_RECENT) {
            h.icon_flecha_item.setVisibility(View.GONE);
            h.delete_recent.setVisibility(View.VISIBLE);
        }else{
            h.icon_flecha_item.setVisibility(View.VISIBLE);
            h.delete_recent.setVisibility(View.GONE);
            h.search_recent_label.setVisibility(View.GONE);
        }

        h.delete_recent.setOnClickListener(v ->{
            data.remove(position);
            listener_recent.deleteRecent(data_parsed.getId_pet());
        });

        h.name_propietary_pet_searching.setText("@"+ ((SearchPetBean) data.get(position)).getName_user());

        h.name_pet_searching.setText(data_parsed.getName_pet() );
        Glide.with(context).load(data_parsed.getUrl_photo())
                .placeholder(context.getResources().getDrawable(R.drawable.ic_holder))
                .error(context.getResources().getDrawable(R.drawable.ic_holder)).into(h.image_pet_searching);
        h.root_pet_searching.setOnClickListener(view -> {
            Bundle b = new Bundle();
            b.putString(BUNDLES.UUID,data_parsed.getUuid());
            b.putInt(BUNDLES.ID_USUARIO,data_parsed.getId_user());
            listener_recent.onSearch(data_parsed,2);
            listener.change_fragment_parameter(FragmentElement.INSTANCE_PREVIEW_PROFILE,b);
        });
        h.name_raza_pet.setText( "  -  " +data_parsed.getName_raza());
        switch (data_parsed.getType_raza()){
            case 1:
                h.icon_type_pet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perro));
                break;
            case 2:
                h.icon_type_pet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gato));
                break;
            case 3:
                h.icon_type_pet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_perico));
                break;
            case 4:
                h.icon_type_pet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_conejo));
                break;
            case 5:
                h.icon_type_pet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_hamster));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SearchPetHolder extends RecyclerView.ViewHolder{
        TextView name_pet_searching,search_recent_label,name_propietary_pet_searching,name_raza_pet;
        ImagenCircular image_pet_searching;
        RelativeLayout root_pet_searching,delete_recent;
        ImageView icon_type_pet;
        ImageView icon_flecha_item;
        public SearchPetHolder(@NonNull View itemView) {
            super(itemView);
            search_recent_label = itemView.findViewById(R.id.search_recent_label);
            name_pet_searching = itemView.findViewById(R.id.name_pet_searching);
            image_pet_searching = itemView.findViewById(R.id.image_pet_searching);
            root_pet_searching = itemView.findViewById(R.id.root_pet_searching);
            delete_recent = itemView.findViewById(R.id.delete_recent);
            name_propietary_pet_searching = itemView.findViewById(R.id.name_propietary_pet_searching);
            icon_type_pet = itemView.findViewById(R.id.icon_type_pet);
            name_raza_pet = itemView.findViewById(R.id.name_raza_pet);
            icon_flecha_item = itemView.findViewById(R.id.icon_flecha_item);
        }
    }
}

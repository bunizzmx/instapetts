package com.bunizz.instapetts.fragments.search.tabs.users;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.SearchUserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.searchRecentListener;
import com.bunizz.instapetts.utils.ImagenCircular;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    changue_fragment_parameters_listener listener;
    boolean HIDE_LABEL =true;
    ArrayList<Object> data = new ArrayList<>();
    boolean IS_RECENT =false;

    searchRecentListener listener_recent;

    public searchRecentListener getListener_recent() {
        return listener_recent;
    }

    public void setListener_recent(searchRecentListener listener_recent) {
        this.listener_recent = listener_recent;
    }

    public boolean isHIDE_LABEL() {
        return HIDE_LABEL;
    }

    public void setHIDE_LABEL(boolean HIDE_LABEL) {
        this.HIDE_LABEL = HIDE_LABEL;
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        Log.e("REFRESH_DATA_SEARCH","--> xxxxxdata:" + data.size());
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear(){
        this.data.clear();;
        notifyDataSetChanged();
    }

    public void is_recent(boolean is_recent){
        IS_RECENT = is_recent;
    }

    public changue_fragment_parameters_listener getListener() {
        return listener;
    }

    public void setListener(changue_fragment_parameters_listener listener) {
        this.listener = listener;
    }

    public SearchUserAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet_searching,parent,false);
        return  new SearchPetHolder(v);
    }

    public  int get_size(){
        return  this.data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchPetHolder h =(SearchPetHolder)holder;
        SearchUserBean data_parsed = (SearchUserBean) data.get(position);
        if(position==0 && HIDE_LABEL == false)
            h.search_recent_label.setVisibility(View.VISIBLE);
        else
            h.search_recent_label.setVisibility(View.GONE);

        h.delete_recent.setOnClickListener(v -> {
            data.remove(position);
            listener_recent.deleteRecent(Integer.parseInt(data_parsed.getId_user()));
        });

        if(HIDE_LABEL == false)
            h.delete_recent.setVisibility(View.VISIBLE);
        else
            h.delete_recent.setVisibility(View.GONE);

        if(IS_RECENT) {
            h.icon_flecha_item.setVisibility(View.GONE);
            h.delete_recent.setVisibility(View.VISIBLE);
        }
        else{
            h.icon_flecha_item.setVisibility(View.VISIBLE);
            h.delete_recent.setVisibility(View.GONE);
            h.search_recent_label.setVisibility(View.GONE);
        }


        h.layout_raza_icon_pet.setVisibility(View.GONE);
        h.name_propietary_pet_searching.setText("@" + data_parsed.getUser_tag());
        h.name_pet_searching.setText(data_parsed.getName_user() );
        Glide.with(context).load(data_parsed.getUrl_photo())
                .placeholder(context.getResources().getDrawable(R.drawable.ic_holder))
                .error(context.getResources().getDrawable(R.drawable.ic_holder))
                .into(h.image_pet_searching);
        h.root_pet_searching.setOnClickListener(view -> {
            Bundle b = new Bundle();
            b.putString(BUNDLES.UUID,data_parsed.getUudi());
            b.putInt(BUNDLES.ID_USUARIO,Integer.parseInt(data_parsed.getId_user()));
            listener_recent.onSearchUser(data_parsed,1);
            listener.change_fragment_parameter(FragmentElement.INSTANCE_PREVIEW_PROFILE,b);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SearchPetHolder extends RecyclerView.ViewHolder{
        TextView name_pet_searching,search_recent_label,name_propietary_pet_searching;
        ImagenCircular image_pet_searching;
        RelativeLayout root_pet_searching,delete_recent;
        LinearLayout layout_raza_icon_pet;
        ImageView icon_flecha_item;
        public SearchPetHolder(@NonNull View itemView) {
            super(itemView);
            name_pet_searching = itemView.findViewById(R.id.name_pet_searching);
            image_pet_searching = itemView.findViewById(R.id.image_pet_searching);
            root_pet_searching = itemView.findViewById(R.id.root_pet_searching);
            search_recent_label = itemView.findViewById(R.id.search_recent_label);
            delete_recent = itemView.findViewById(R.id.delete_recent);
            name_propietary_pet_searching = itemView.findViewById(R.id.name_propietary_pet_searching);
            layout_raza_icon_pet = itemView.findViewById(R.id.layout_raza_icon_pet);
            icon_flecha_item = itemView.findViewById(R.id.icon_flecha_item);
        }
    }
}

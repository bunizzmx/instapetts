package com.bunizz.instapetts.fragments.search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.SearchUserBean;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.utils.ImagenCircular;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    change_instance_wizard listener;

    ArrayList<Object> data = new ArrayList<>();

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        Log.e("REFRESH_DATA_SEARCH","--> xxxxxdata:" + data.size());
        this.data = data;
        notifyDataSetChanged();
    }

    public change_instance_wizard getListener() {
        return listener;
    }

    public void setListener(change_instance_wizard listener) {
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchPetHolder h =(SearchPetHolder)holder;
        SearchUserBean data_parsed = (SearchUserBean) data.get(position);
        h.name_pet_searching.setText(data_parsed.getName_user() );
        Glide.with(context).load(data_parsed.getUrl_photo()).into(h.image_pet_searching);
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

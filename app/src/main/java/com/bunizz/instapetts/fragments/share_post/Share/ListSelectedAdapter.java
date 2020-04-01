package com.bunizz.instapetts.fragments.share_post.Share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.listeners.change_instance_wizard;

import java.util.ArrayList;

public class ListSelectedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    change_instance_wizard listener;

    ArrayList<String> data = new ArrayList<>();

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public change_instance_wizard getListener() {
        return listener;
    }

    public void setListener(change_instance_wizard listener) {
        this.listener = listener;
    }

    public ListSelectedAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_image,parent,false);
        return  new ListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListHolder h =(ListHolder)holder;
        Glide.with(context).load(data.get(position)).into(h.image_selected);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder{
        ImageView image_selected;
        public ListHolder(@NonNull View itemView) {
            super(itemView);
            image_selected = itemView.findViewById(R.id.image_selected);
        }
    }
}

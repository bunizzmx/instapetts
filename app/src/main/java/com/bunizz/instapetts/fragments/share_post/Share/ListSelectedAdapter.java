package com.bunizz.instapetts.fragments.share_post.Share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.listeners.remove_litener;

import java.util.ArrayList;

public class ListSelectedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    change_instance_wizard listener;

    ArrayList<String> data = new ArrayList<>();
    remove_litener listener_remove;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data.clear();
        this.data.addAll(data);
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

    public remove_litener getListener_remove() {
        return listener_remove;
    }

    public void setListener_remove(remove_litener listener_remove) {
        this.listener_remove = listener_remove;
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
        h.delete_selection.setVisibility(View.VISIBLE);
        h.delete_selection.setOnClickListener(v -> {
            data.remove(position);
            notifyDataSetChanged();
            listener_remove.remove(position);
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder{
        ImageView image_selected;
        CardView delete_selection;
        public ListHolder(@NonNull View itemView) {
            super(itemView);
            image_selected = itemView.findViewById(R.id.image_selected);
            delete_selection = itemView.findViewById(R.id.delete_selection);
        }
    }
}

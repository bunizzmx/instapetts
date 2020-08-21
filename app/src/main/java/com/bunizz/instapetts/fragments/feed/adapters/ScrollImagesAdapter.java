package com.bunizz.instapetts.fragments.feed.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.utils.imagePicker.data.Image;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<String> image_urls = new ArrayList<>();
    String uris_not_parsed ="";


    public ScrollImagesAdapter(Context context) {
        this.context = context;
    }


    public void setUris_not_parsed(String uris_not_parsed) {
        this.uris_not_parsed = uris_not_parsed;
        image_urls.clear();
        String parsed[]  = uris_not_parsed.split(",");
        if(parsed.length == 1 && uris_not_parsed.length() > 0 ){
            Log.e("DATA_PARSED","solo es una");
            image_urls.add(uris_not_parsed);
        }else{
            Log.e("DATA_PARSED","son varias");
            for (int i =0;i< parsed.length;i++){
                image_urls.add(parsed[i]);
            }
        }
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_galery,parent,false);
       return new ScrollHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScrollHolder h =(ScrollHolder)holder;
        Glide.with(context).load(image_urls.get(position)).into( h.image_scroll);

    }

    @Override
    public int getItemCount() {
        return image_urls.size();
    }

    public class ScrollHolder extends RecyclerView.ViewHolder{
        ImageView image_scroll;
        public ScrollHolder(@NonNull View itemView) {
            super(itemView);
            image_scroll = itemView.findViewById(R.id.image_scroll);
        }
    }
}

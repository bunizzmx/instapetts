package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.camera_history.CameraHistoryActivity;
import com.bunizz.instapetts.utils.HistoryView.StoryPlayer;
import com.bunizz.instapetts.utils.ImagenCircular;


public class FeedAdapterHistories extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    public FeedAdapterHistories(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return  new FeedHistoriesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FeedHistoriesHolder h =(FeedHistoriesHolder)holder;
        //Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/melove-principal/o/C18%2F15cb3831f0b9426c484d380f0ab1afac.jpg?alt=media&token=042d5974-e96c-4bcc-9fa2-f657fe2167af").into(h.image_pet_history);

        if(position == 0){
            h.image_pet_history.setStroke_separate(false);
            h.image_pet_history.setBorderColor(Color.WHITE);
            h.image_pet_history.setOnClickListener(view -> {
                Intent i = new Intent(context, CameraHistoryActivity.class);
                context.startActivity(i);
            });
            h.icon_add_story_user.setVisibility(View.VISIBLE);

        }else{
            h.image_pet_history.setBorderColor(context.getResources().getColor(R.color.naranja));
            h.image_pet_history.setStroke_separate(true);
            h.icon_add_story_user.setVisibility(View.GONE);
            h.image_pet_history.setOnClickListener(view -> {
                Intent i = new Intent(context, StoryPlayer.class);
                context.startActivity(i);
            });
        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class FeedHistoriesHolder extends RecyclerView.ViewHolder{
     ImagenCircular image_pet_history;
     RelativeLayout icon_add_story_user;
        public FeedHistoriesHolder(@NonNull View itemView) {
            super(itemView);
            image_pet_history = itemView.findViewById(R.id.image_pet_history);
            icon_add_story_user = itemView.findViewById(R.id.icon_add_story_user);
        }
    }
}

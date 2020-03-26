package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.camera_history.CameraHistoryActivity;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.listeners.open_camera_histories_listener;
import com.bunizz.instapetts.utils.HistoryView.StoryPlayer;
import com.bunizz.instapetts.utils.ImagenCircular;

import org.parceler.Parcels;

import java.util.ArrayList;


public class FeedAdapterHistories extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<HistoriesBean> historiesBeans = new ArrayList<>();
    open_camera_histories_listener listener;

    public ArrayList<HistoriesBean> getHistoriesBeans() {
        return historiesBeans;
    }

    public void setHistoriesBeans(ArrayList<HistoriesBean> historiesBeans) {
        if(historiesBeans!=null) {
            this.historiesBeans.addAll(historiesBeans);
            notifyDataSetChanged();
        }
    }

    public open_camera_histories_listener getListener() {
        return listener;
    }

    public void setListener(open_camera_histories_listener listener) {
        this.listener = listener;
    }

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
        if(position == 0){
            h.name_pet_item.setText("Tu History");
            if(historiesBeans.get(position)!=null){
                if(historiesBeans.get(position).getUris_stories()!=null){
                    h.image_pet_history.setBorderColor(context.getResources().getColor(R.color.primary));
                    h.profile_background.setOnClickListener(view -> {
                            Intent i = new Intent(context, StoryPlayer.class);
                            i.putExtra("sliders", Parcels.wrap(historiesBeans));
                            i.putExtra("SELECTED_POSITION", position);
                            context.startActivity(i);
                    });
                    h.icon_add_story_user.setVisibility(View.GONE);
                    Glide.with(context).load(historiesBeans.get(position).getUris_stories()).into(h.profile_background);
                }else{
                    h.image_pet_history.setStroke_separate(false);
                    h.image_pet_history.setBorderColor(Color.WHITE);
                    h.profile_background.setOnClickListener(view -> {
                        if(listener!=null){
                            listener.open();
                        }

                    });
                    h.icon_add_story_user.setVisibility(View.VISIBLE);
                }
            }else{
                Log.e("MY_STORIE","null all ");
            }
            Glide.with(context).load(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID")).placeholder(context.getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(h.image_pet_history);
        }else{
            Glide.with(context).load(historiesBeans.get(position).getUrl_photo_user()).into(h.image_pet_history);
            Glide.with(context).load(historiesBeans.get(position).getUris_stories()).into(h.profile_background);
            h.image_pet_history.setBorderColor(context.getResources().getColor(R.color.primary));
            h.icon_add_story_user.setVisibility(View.GONE);
            h.profile_background.setOnClickListener(view -> {
                Intent i = new Intent(context, StoryPlayer.class);
                i.putExtra("sliders", Parcels.wrap(historiesBeans));
                i.putExtra("SELECTED_POSITION", position);
                context.startActivity(i);
            });
            h.name_pet_item.setText(historiesBeans.get(position).getName_user());
        }

    }

    @Override
    public int getItemCount() {
        return historiesBeans.size();
    }

    public class FeedHistoriesHolder extends RecyclerView.ViewHolder{
     ImagenCircular image_pet_history;
     RelativeLayout icon_add_story_user;
     TextView name_pet_item;
     ImageView profile_background;
        public FeedHistoriesHolder(@NonNull View itemView) {
            super(itemView);
            image_pet_history = itemView.findViewById(R.id.image_pet_history);
            icon_add_story_user = itemView.findViewById(R.id.icon_add_story_user);
            name_pet_item = itemView.findViewById(R.id.name_pet_item);
            profile_background = itemView.findViewById(R.id.profile_background);
        }
    }


}
